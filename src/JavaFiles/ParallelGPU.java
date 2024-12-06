package JavaFiles;
import org.jocl.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import static org.jocl.CL.*;

public class ParallelGPU {
    private final String targetWord;
    private HashMap<String, ArrayList<Integer>> results;

    public ParallelGPU(String targetWord) {
        this.targetWord = targetWord.toLowerCase();
        this.results = new HashMap<>();
    }

    public HashMap<String, ArrayList<Integer>> count(File assets) {
        if (!assets.exists() || !assets.isDirectory()) {
            System.out.println("O diretório 'assets' não existe ou não é válido.");
            return null;
        }

        for (File file : assets.listFiles()) {
            if (file.isFile()) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append(" ");
                    }

                    String text = sb.toString();
                    long startTime = System.currentTimeMillis();
                    int wordCount = countWordsOnGPU(text);
                    long endTime = System.currentTimeMillis();

                    // Armazena resultados
                    results.put(file.getName(), new ArrayList<>() {{
                        add(wordCount);
                        add((int) (endTime - startTime));
                    }});
                } catch (IOException e) {
                    System.out.println("Erro ao processar o arquivo: " + file.getName());
                    e.printStackTrace();
                }
            }
        }
        return results;
    }

    private int countWordsOnGPU(String text) {
        // Configuração do OpenCL
        CL.setExceptionsEnabled(true);

        // Obter plataforma e dispositivo
        cl_platform_id[] platforms = new cl_platform_id[1];
        clGetPlatformIDs(1, platforms, null);
        cl_platform_id platform = platforms[0];

        cl_device_id[] devices = new cl_device_id[1];
        clGetDeviceIDs(platform, CL_DEVICE_TYPE_GPU, 1, devices, null);
        cl_device_id device = devices[0];

        cl_context context = clCreateContext(null, 1, devices, null, null, null);
        cl_command_queue commandQueue = clCreateCommandQueueWithProperties(context, device, null, null);

        // Transformar texto e palavra-alvo em arrays de bytes
        byte[] textBytes = text.getBytes();
        byte[] targetBytes = targetWord.getBytes();
        int textLength = textBytes.length;
        int targetLength = targetBytes.length;
        int[] result = new int[1];

        // Alocar buffers na GPU
        cl_mem textBuffer = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_char * textLength, Pointer.to(textBytes), null);
        cl_mem targetBuffer = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_char * targetLength, Pointer.to(targetBytes), null);
        cl_mem resultBuffer = clCreateBuffer(context, CL_MEM_WRITE_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int, Pointer.to(result), null);

        // Kernel OpenCL
        String programSource = "__kernel void wordCount(__global const char* text, "
                + "__global const char* target, "
                + "__global int* result, "
                + "const int textLength, const int targetLength) {"
                + "    int gid = get_global_id(0);"
                + "    int localCount = 0;"
                + "    if (gid + targetLength <= textLength) {"
                + "        int match = 1;"
                + "        for (int i = 0; i < targetLength; i++) {"
                + "            if (text[gid + i] != target[i]) {"
                + "                match = 0;"
                + "                break;"
                + "            }"
                + "        }"
                + "        if (match) {"
                + "            localCount = 1;"
                + "        }"
                + "    }"
                + "    atomic_add(result, localCount);"
                + "}";

        cl_program program = clCreateProgramWithSource(context, 1, new String[]{programSource}, null, null);
        clBuildProgram(program, 0, null, null, null, null);

        cl_kernel kernel = clCreateKernel(program, "wordCount", null);

        // Configurar argumentos do kernel
        clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(textBuffer));
        clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(targetBuffer));
        clSetKernelArg(kernel, 2, Sizeof.cl_mem, Pointer.to(resultBuffer));
        clSetKernelArg(kernel, 3, Sizeof.cl_int, Pointer.to(new int[]{textLength}));
        clSetKernelArg(kernel, 4, Sizeof.cl_int, Pointer.to(new int[]{targetLength}));

        // Executar kernel
        long globalWorkSize[] = new long[]{textLength};
        clEnqueueNDRangeKernel(commandQueue, kernel, 1, null, globalWorkSize, null, 0, null, null);

        // Ler o resultado
        clEnqueueReadBuffer(commandQueue, resultBuffer, CL_TRUE, 0, Sizeof.cl_int, Pointer.to(result), 0, null, null);

        // Liberar recursos
        clReleaseKernel(kernel);
        clReleaseProgram(program);
        clReleaseMemObject(textBuffer);
        clReleaseMemObject(targetBuffer);
        clReleaseMemObject(resultBuffer);
        clReleaseCommandQueue(commandQueue);
        clReleaseContext(context);

        return result[0];
    }
}
