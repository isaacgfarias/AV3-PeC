Introdução

Este trabalho tem como objetivo analisar detalhadamente o desempenho de diferentes métodos para contagem de palavras em arquivos de texto, considerando as abordagens serial e paralelas. A crescente demanda por eficiência em processamento de dados motiva o estudo de algoritmos que exploram arquiteturas multicore (CPU) e paralelismo em GPUs. Para isso, foram implementados três métodos: (1) execução serial utilizando a CPU, (2) execução paralela na CPU com divisão de tarefas entre múltiplos threads e (3) execução paralela na GPU utilizando OpenCL.

O estudo utiliza arquivos de texto como base de dados e mede o desempenho de cada método em diferentes cenários, incluindo tamanhos variados de arquivos e configurações de hardware. Os resultados são analisados em termos de tempo de execução e contagem de palavras corretas, permitindo identificar padrões e limitações de cada abordagem. Os resultados são exportados para arquivos CSV e apresentados em gráficos para facilitar a interpretação.

Dessa forma, o trabalho busca contribuir para o entendimento do impacto de diferentes estratégias de paralelismo no desempenho de algoritmos de processamento de texto, fornecendo uma análise comparativa fundamentada para orientar escolhas em cenários reais.

Metodologia

Para a realização deste trabalho, foi adotada uma abordagem experimental baseada em análise estatística dos resultados obtidos. Foram implementados três métodos de contagem de palavras em Java:

SerialCPU: Um algoritmo serial que itera sequencialmente sobre cada palavra do texto para contar as ocorrências da palavra-alvo.
ParallelCPU: Um algoritmo paralelo que divide o texto em partes e utiliza múltiplos threads para realizar a contagem de palavras em paralelo, explorando a arquitetura multicore da CPU.
ParallelGPU: Um algoritmo que utiliza OpenCL para realizar a contagem de palavras na GPU, explorando o paralelismo massivo dessa arquitetura.
Os experimentos foram conduzidos em arquivos de texto de tamanhos variados, com o objetivo de analisar o impacto do tamanho do conjunto de dados no desempenho dos algoritmos. Para cada cenário, foram realizadas múltiplas execuções para garantir a representatividade dos resultados. Os tempos de execução e as contagens de palavras foram registrados em arquivos CSV para posterior análise.

Além disso, foram gerados gráficos que ilustram as diferenças de desempenho entre os métodos sob diferentes condições de entrada e configurações de hardware. Por fim, o impacto do ajuste do número de núcleos disponíveis para processamento no desempenho do método ParallelCPU foi investigado, adicionando uma camada de análise comparativa entre configurações de execução paralela.

Os resultados apresentados visam identificar padrões de desempenho e limitações de cada método, contribuindo para a compreensão das vantagens e desafios do uso de algoritmos paralelos em diferentes arquiteturas computacionais.



Resultados

A análise comparativa dos tempos de execução dos três métodos de contagem de palavras revelou diferenças significativas de desempenho entre as abordagens serial e paralelas, considerando diferentes tamanhos de arquivos de texto. Os resultados obtidos foram os seguintes:

ParallelGPU: Embora a abordagem paralela utilizando GPU tenha apresentado a maior capacidade de paralelismo teórico, ela demonstrou o maior tempo de execução em todos os arquivos analisados. Por exemplo, no arquivo menor, Dracula-165307.txt, o tempo de execução foi de 309 ms, enquanto no arquivo maior, DonQuixote-388208.txt, alcançou 506 ms. Isso sugere que o overhead associado à inicialização e gerenciamento do processamento na GPU superou os benefícios do paralelismo para os tamanhos de texto avaliados.

ParallelCPU: A execução paralela na CPU foi a mais eficiente em termos de tempo de execução. Para o arquivo Dracula-165307.txt, o método levou apenas 9 ms, e para o arquivo maior, DonQuixote-388208.txt, levou 25 ms. Esses resultados evidenciam a eficiência do modelo de divisão de tarefas com múltiplos threads na arquitetura multicore, especialmente para dados de tamanhos variados.

SerialCPU: A abordagem serial apresentou tempos de execução intermediários. No arquivo menor, Dracula-165307.txt, o método consumiu 148 ms, enquanto para o arquivo maior, DonQuixote-388208.txt, o tempo subiu para 151 ms. Esses resultados mostram que, embora a abordagem seja menos complexa, sua performance é significativamente limitada pela ausência de paralelismo.

Os dados demonstram que a execução paralela em CPU foi claramente superior para os conjuntos de dados analisados, apresentando tempos de execução consistentemente menores que as abordagens serial e paralela em GPU.

Conclusão

Os resultados obtidos neste estudo confirmam que a escolha da abordagem de processamento tem um impacto significativo no desempenho dos algoritmos de contagem de palavras. Apesar de a GPU ser projetada para paralelismo massivo, os tempos de execução elevados indicam que o overhead associado à sua utilização pode ser um fator limitante em cenários com arquivos de texto de tamanho moderado, como os utilizados neste experimento. Por outro lado, a abordagem paralela na CPU mostrou-se a mais eficiente, alcançando os menores tempos de execução devido à menor sobrecarga e à utilização eficaz dos núcleos disponíveis.

A abordagem serial, embora simples, destacou-se por sua previsibilidade e desempenho aceitável, mas ficou atrás das abordagens paralelas em cenários onde múltiplos núcleos poderiam ser explorados.

Portanto, conclui-se que a escolha da técnica ideal depende do tamanho dos dados e da infraestrutura disponível. Para cenários semelhantes aos analisados, a execução paralela na CPU é a recomendação mais apropriada. Estudos futuros podem explorar o impacto de conjuntos de dados significativamente maiores ou diferentes arquiteturas de GPU para avaliar se o benefício do paralelismo massivo se torna mais evidente em outras condições.

Referências:
"Introduction to Parallel Computing" – Ananth Grama, George Karypis, Vipin Kumar e Anshul Gupta
"Java Performance: The Definitive Guide" – Scott Oaks
"Evaluation of Multithreading and GPU-based Parallelism for Text Processing" – Journal of Parallel and Distributed Computing
Java Documentation – Concurrency Framework