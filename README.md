# Aplicação Flood Fill em Java

Uma aplicação de linha de comando que implementa o algoritmo de flood fill com geração de animação GIF, mostrando o processo de preenchimento passo a passo.

## Características

- **Estruturas de dados personalizadas**: Implementação própria de Lista, Fila, Pilha e Elemento
- **Dois algoritmos de flood fill**: 
  - **Fila (BFS - Breadth-First Search)**: Preenche de forma mais uniforme, espalhando em todas as direções
  - **Pilha (DFS - Depth-First Search)**: Preenche seguindo caminhos mais profundos, criando padrões diferentes
- **Seleção interativa do algoritmo**: Escolha entre BFS e DFS durante a execução
- **Geração de frames**: Salva o progresso da animação em arquivos PNG
- **Criação de GIF**: Converte os frames em um GIF animado
- **Interface de linha de comando**: Interação amigável com o usuário

## Estrutura do Projeto

```
TDE1/
├── src/
│   ├── estruturas/
│   │   ├── Elemento.java      # Elemento genérico para estruturas
│   │   ├── Lista.java         # Lista encadeada
│   │   ├── Fila.java          # Fila (FIFO)
│   │   ├── Pilha.java         # Pilha (LIFO)
│   │   ├── Ponto.java         # Ponto (Cordenadas)
│   │   └── TipoAlgoritmo.java # Enum para tipos de algoritmo
│   ├── Main.java              # Classe principal
│   ├── FloodFill.java         # Algoritmo de flood fill
│   ├── GifSequenceWriter.java # Algoritmo de geração de gif (Pronto)
│   └── GeradorGIF.java        # Geração de GIF
├── classes/                   # Pasta das classes              
│   ├── frames/                # Pasta para os frames PNG
│   └── resultado.gif          # Resultado do Gif
└── README.md
```


## Bibliotecas Utilizadas (Imports)

O projeto utiliza as seguintes bibliotecas Java:

```java
// Manipulação de imagens
import java.awt.Color;
import java.awt.image.BufferedImage;

// Operações de arquivo
import java.io.File;
import java.io.IOException;

// Leitura/escrita de imagens
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

// Estruturas de dados personalizadas
import estruturas.Lista;
import estruturas.Fila;
import estruturas.Pilha;
import estruturas.Elemento;
import estruturas.TipoAlgoritmo;

// Utilidades
import java.util.Scanner;
import java.util.Arrays; // (usado apenas se necessário para ordenação)
```

Essas bibliotecas são usadas para manipulação de imagens, arquivos, entrada do usuário, e para implementar as estruturas de dados personalizadas do projeto.

## Como Usar

### Pré-requisitos

1. Java 8 ou superior

### Compilação e Execução

1. Linux
```bash
cd /Users/fergrenteski/TDE1
./compilar_e_executar.sh
```

1. Windows
```bash
cd /Users/fergrenteski/TDE1
./compilar_e_executar.bat
```


### Fluxo da Aplicação

1. **Caminho da imagem**: Digite o caminho completo para o arquivo de imagem
2. **Seleção do algoritmo**: Escolha entre Fila (BFS) ou Pilha (DFS)
3. **Informações da imagem**: Visualize as dimensões e algoritmo selecionado
4. **Intervalo de frames**: Defina a cada quantos pixels salvar um frame
5. **Seleção de cor**: Escolha a cor de preenchimento (predefinida ou personalizada)
6. **Coordenadas**: Digite as coordenadas X,Y para iniciar o flood fill
7. **Confirmação**: Confirme a execução
8. **Processamento**: Aguarde a conclusão do flood fill
9. **Configuração do GIF**: Defina nome e delay entre frames
10. **Resultado**: GIF gerado na pasta do projeto

## Exemplo de Uso

```
=== APLICAÇÃO FLOOD FILL ===

Digite o caminho da imagem: exemplo.png
Imagem carregada com sucesso!
Dimensões: 800 x 600 pixels
Total de pixels: 480000

A cada quantos pixels salvar um frame? (padrão: 100): 50

Seleção de cor:
1 - Vermelho
2 - Verde
3 - Azul
Escolha uma opção (1-3): 1

Selecione as coordenadas para iniciar o flood fill:
Limites: X (0 a 799), Y (0 a 599)
Digite a coordenada X: 400
Digite a coordenada Y: 300

Cor atual no pixel (400, 300): R=255, G=255, B=255

Deseja executar o flood fill? (s/n): s
Executando flood fill...
Flood fill concluído em 1234 ms
Número de frames gerados: 156

Salvos 156 frames na pasta: frames

Digite o nome do arquivo GIF (sem extensão): minha_animacao
Digite o delay entre frames em milissegundos (padrão: 100): 80
Gerando GIF...
GIF gerado com sucesso: minha_animacao.gif

Processo concluído com sucesso!
```

## Algoritmos Flood Fill

### Fila (BFS - Breadth-First Search)
- **Comportamento**: Preenche de forma mais uniforme, espalhando em todas as direções simultaneamente
- **Padrão visual**: Crescimento circular/radial a partir do ponto inicial
- **Características**: Explora todos os pixels adjacentes antes de ir para os mais distantes
- **Melhor para**: Criar animações com crescimento uniforme e suave

### Pilha (DFS - Depth-First Search) 
- **Comportamento**: Preenche seguindo caminhos mais profundos antes de explorar outras direções
- **Padrão visual**: Crescimento em "tentáculos" ou ramificações
- **Características**: Segue um caminho até o fim antes de voltar e explorar outras direções
- **Melhor para**: Criar animações com padrões mais orgânicos e interessantes

### Comparação Visual
Os dois algoritmos preenchem a mesma área final, mas criam animações completamente diferentes:
- **BFS**: Crescimento uniforme como uma onda
- **DFS**: Crescimento ramificado como raízes ou veias

## Tratamento de Exceções

- Verifica se o arquivo de imagem existe e é válido
- Valida coordenadas dentro dos limites da imagem
- Trata entradas inválidas do usuário
- Gerencia erros de E/S durante o processamento

## Algoritmo Flood Fill

O algoritmo implementado é iterativo e usa uma fila para:
1. Evitar estouro de pilha em imagens grandes
2. Garantir preenchimento uniforme
3. Permitir salvamento de frames durante o processo
