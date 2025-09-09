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
│   │   └── TipoAlgoritmo.java # Enum para tipos de algoritmo
│   ├── Main.java              # Classe principal
│   ├── FloodFill.java         # Algoritmo de flood fill
│   ├── Ponto.java             # Classe para coordenadas
│   └── GeradorGIF.java        # Geração de GIF
├── frames/                    # Pasta para os frames PNG
└── README.md
```

## Bibliotecas Utilizadas

- `java.awt.image.BufferedImage` - Para manipulação de imagens
- `java.io.File` - Para operações de arquivo
- `javax.imageio.ImageIO` - Para leitura/escrita de imagens

## Como Usar

### Pré-requisitos

1. Java 8 ou superior
2. ImageMagick (opcional, para geração automática de GIF)
   - macOS: `brew install imagemagick`
   - Ubuntu/Debian: `sudo apt-get install imagemagick`
   - Windows: Baixar do site oficial

### Compilação

```bash
cd /Users/fergrenteski/TDE1
javac -d . src/*.java src/estruturas/*.java
```

### Execução

```bash
java Main
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
GIF gerado com sucesso usando ImageMagick: minha_animacao.gif

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

## Geração de GIF

1. **Primeiro tenta usar ImageMagick** (método recomendado)
2. **Se ImageMagick não estiver disponível**, cria um script shell para geração manual
3. **Configurações personalizáveis**: delay entre frames, nome do arquivo

## Limitações

- Funciona apenas com formatos de imagem suportados pelo Java (PNG, JPG, GIF, BMP)
- Requer ImageMagick para geração automática de GIF
- O tempo de processamento depende do tamanho da área a ser preenchida

## Possíveis Melhorias

- Suporte a diferentes algoritmos de flood fill
- Interface gráfica
- Otimizações de performance
- Suporte a mais formatos de imagem
- Geração de GIF nativa em Java
