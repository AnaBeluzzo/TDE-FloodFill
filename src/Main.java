import java.awt.Color;
import java.io.IOException;
import java.util.Scanner;
import estruturas.TipoAlgoritmo;

/**
 * Classe principal da aplicação Flood Fill
 * Gerencia a interação com o usuário via linha de comando
 */
public class Main {
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("=== APLICAÇÃO FLOOD FILL ===");
        System.out.println();
        
        try {
            // 1. Solicitar caminho da imagem
            String caminhoImagem = solicitarCaminhoImagem();
            
            // 2. Selecionar tipo de algoritmo
            TipoAlgoritmo tipoAlgoritmo = selecionarTipoAlgoritmo();
            
            // 3. Carregar imagem e criar objeto FloodFill
            FloodFill floodFill = new FloodFill(caminhoImagem, tipoAlgoritmo);
            
            // 4. Mostrar informações da imagem
            mostrarInformacoesImagem(floodFill);
            
            // 5. Configurar intervalo de frames
            int intervaloPorFrame = solicitarIntervaloPorFrame(floodFill);
            floodFill.setIntervaloPorFrame(intervaloPorFrame);
            
            // 6. Selecionar cor de preenchimento
            Color novaCor = selecionarCor();
            
            // 7. Selecionar coordenadas
            int[] coordenadas = selecionarCoordenadas(floodFill);
            int x = coordenadas[0];
            int y = coordenadas[1];
            
            // Mostrar cor atual do pixel selecionado
            Color corAtual = floodFill.getCorPixel(x, y);
            System.out.printf("Cor atual no pixel (%d, %d): R=%d, G=%d, B=%d%n", 
                            x, y, corAtual.getRed(), corAtual.getGreen(), corAtual.getBlue());
            
            // 8. Confirmar execução
            if (confirmarExecucao()) {
                System.out.println("Executando flood fill...");
                
                long tempoInicio = System.currentTimeMillis();
                floodFill.executarFloodFill(x, y, novaCor);
                long tempoFim = System.currentTimeMillis();
                
                System.out.printf("Flood fill concluído em %d ms%n", (tempoFim - tempoInicio));
                System.out.println("Número de frames gerados: " + floodFill.getNumeroFrames());
                
                // 9. Salvar frames
                String pastaFrames = "frames";
                floodFill.salvarFrames(pastaFrames);
                
                // 9. Gerar GIF
                String nomeGIF = solicitarNomeGIF();
                int delayGIF = solicitarDelayGIF();
                
                System.out.println("Gerando GIF...");
                GeradorGIF.gerarGIF(pastaFrames, nomeGIF, delayGIF);
                
                System.out.println("Processo concluído com sucesso!");
            } else {
                System.out.println("Operação cancelada.");
            }
            
        } catch (IOException e) {
            System.err.println("Erro de E/S: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Erro nos parâmetros: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
    
    private static String solicitarCaminhoImagem() {
        System.out.print("Digite o caminho da imagem: ");
        return scanner.nextLine().trim();
    }
    
    private static TipoAlgoritmo selecionarTipoAlgoritmo() {
        System.out.println("\nSelecione o tipo de algoritmo de flood fill:");
        System.out.println("1 - " + TipoAlgoritmo.FILA_BFS.getDescricao());
        System.out.println("2 - " + TipoAlgoritmo.PILHA_DFS.getDescricao());
        System.out.println();
        System.out.println("Diferenças:");
        System.out.println("• Fila (BFS): Preenche de forma mais uniforme, espalhando em todas as direções");
        System.out.println("• Pilha (DFS): Preenche seguindo caminhos mais profundos, criando padrões diferentes");
        
        while (true) {
            try {
                System.out.print("Escolha uma opção (1-2): ");
                int opcao = Integer.parseInt(scanner.nextLine().trim());
                
                switch (opcao) {
                    case 1:
                        System.out.println("Algoritmo selecionado: " + TipoAlgoritmo.FILA_BFS.getDescricao());
                        return TipoAlgoritmo.FILA_BFS;
                    case 2:
                        System.out.println("Algoritmo selecionado: " + TipoAlgoritmo.PILHA_DFS.getDescricao());
                        return TipoAlgoritmo.PILHA_DFS;
                    default:
                        System.out.println("Opção inválida. Digite 1 ou 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido.");
            }
        }
    }
    
    private static void mostrarInformacoesImagem(FloodFill floodFill) {
        System.out.printf("Imagem carregada com sucesso!%n");
        System.out.printf("Algoritmo: %s%n", floodFill.getTipoAlgoritmo().getDescricao());
        System.out.printf("Dimensões: %d x %d pixels%n", 
                        floodFill.getLargura(), floodFill.getAltura());
        System.out.printf("Total de pixels: %d%n", 
                        floodFill.getLargura() * floodFill.getAltura());
    }
    
    private static int solicitarIntervaloPorFrame(FloodFill floodFill) {
        int totalPixels = floodFill.getLargura() * floodFill.getAltura();
        int intervaloMinimo = Math.max(totalPixels / 500, 50); // Garante máximo 100 frames e mínimo 100 pixels
        
        System.out.printf("Total de pixels da imagem: %d%n", totalPixels);
        System.out.printf("Intervalo mínimo recomendado para evitar out of memory: %d pixels%n", intervaloMinimo);
        
        while (true) {
            try {
                System.out.printf("A cada quantos pixels salvar um frame? (mínimo: %d, padrão: %d): ", 
                                 intervaloMinimo, intervaloMinimo);
                String input = scanner.nextLine().trim();
                
                if (input.isEmpty()) {
                    return intervaloMinimo; // valor padrão seguro
                }
                
                int intervalo = Integer.parseInt(input);
                if (intervalo >= intervaloMinimo) {
                    return intervalo;
                } else {
                    System.out.printf("Por favor, digite um número maior ou igual a %d para evitar problemas de memória.%n", 
                                     intervaloMinimo);
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido.");
            }
        }
    }
    
    private static Color selecionarCor() {
        System.out.println("\nSeleção de cor:");
        System.out.println("1 - Vermelho");
        System.out.println("2 - Verde");
        System.out.println("3 - Azul");
        
        while (true) {
            try {
                System.out.print("Escolha uma opção (1-9): ");
                int opcao = Integer.parseInt(scanner.nextLine().trim());
                
                switch (opcao) {
                    case 1: return Color.RED;
                    case 2: return Color.GREEN;
                    case 3: return Color.BLUE;
                    default:
                        System.out.println("Opção inválida. Digite um número de 1 a 3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido.");
            }
        }
    }
    
    private static Color solicitarCorPersonalizada() {
        while (true) {
            try {
                System.out.print("Digite o valor R (0-255): ");
                int r = Integer.parseInt(scanner.nextLine().trim());
                
                System.out.print("Digite o valor G (0-255): ");
                int g = Integer.parseInt(scanner.nextLine().trim());
                
                System.out.print("Digite o valor B (0-255): ");
                int b = Integer.parseInt(scanner.nextLine().trim());
                
                if (r >= 0 && r <= 255 && g >= 0 && g <= 255 && b >= 0 && b <= 255) {
                    return new Color(r, g, b);
                } else {
                    System.out.println("Os valores RGB devem estar entre 0 e 255.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite números válidos.");
            }
        }
    }
    
    private static int[] selecionarCoordenadas(FloodFill floodFill) {
        int largura = floodFill.getLargura();
        int altura = floodFill.getAltura();
        
        System.out.printf("%nSelecione as coordenadas para iniciar o flood fill:%n");
        System.out.printf("Limites: X (0 a %d), Y (0 a %d)%n", largura - 1, altura - 1);
        
        while (true) {
            try {
                System.out.print("Digite a coordenada X: ");
                int x = Integer.parseInt(scanner.nextLine().trim());
                
                System.out.print("Digite a coordenada Y: ");
                int y = Integer.parseInt(scanner.nextLine().trim());
                
                if (x >= 0 && x < largura && y >= 0 && y < altura) {
                    return new int[]{x, y};
                } else {
                    System.out.printf("Coordenadas fora dos limites! X deve estar entre 0 e %d, Y entre 0 e %d.%n", 
                                    largura - 1, altura - 1);
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite números válidos.");
            }
        }
    }
    
    private static boolean confirmarExecucao() {
        System.out.println("\n=== RESUMO DA CONFIGURAÇÃO ===");
        System.out.print("Deseja executar o flood fill? (s/n): ");
        String resposta = scanner.nextLine().trim().toLowerCase();
        return resposta.equals("s") || resposta.equals("sim") || resposta.equals("y") || resposta.equals("yes");
    }
    
    private static String solicitarNomeGIF() {
        System.out.print("Digite o nome do arquivo GIF (sem extensão, padrão: resultado): ");
        String nome = scanner.nextLine().trim();
        return nome.isEmpty() ? "resultado.gif" : nome + ".gif";
    }
    
    private static int solicitarDelayGIF() {
        while (true) {
            try {
                System.out.print("Digite o delay entre frames em milissegundos (padrão: 100): ");
                String input = scanner.nextLine().trim();
                
                if (input.isEmpty()) {
                    return 100; // valor padrão
                }
                
                int delay = Integer.parseInt(input);
                if (delay > 0) {
                    return delay;
                } else {
                    System.out.println("Por favor, digite um número positivo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido.");
            }
        }
    }
}
