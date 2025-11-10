import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

/**
 * PainelDoJogo é o componente principal que gerencia o tabuleiro,
 * os inputs do usuário (mouse) e o estado do jogo.
 */
public class PainelDoJogo extends JPanel {

    // --- Constantes do Tabuleiro ---
    public static final int LINHAS = 8;
    public static final int COLUNAS = 8;
    public static final int TAMANHO_CELULA = 70; // 70 pixels por célula

    // --- Estado do Jogo ---
    private Matriz matriz;
    
    // O traço que o usuário está ativamente desenhando
    private Traco tracoAtual; 

    /**
     * Construtor
     */
    public PainelDoJogo() {
        // 1. Configurar o painel
        int largura = COLUNAS * TAMANHO_CELULA;
        int altura = LINHAS * TAMANHO_CELULA;
        setPreferredSize(new Dimension(largura, altura));
        setBackground(new Color(45, 45, 55)); // Fundo escuro

        // 2. Inicializar o "cérebro" (Matriz)
        this.matriz = new Matriz(LINHAS, COLUNAS, TAMANHO_CELULA);
        
        // 3. Carregar o nível
        this.matriz.carregarNivelExemplo(); // Usando o método que criamos na Matriz

        // 4. Inicializar o traço atual como nulo
        this.tracoAtual = null;

        // 5. Adicionar os "ouvintes" de mouse
        MouseHandler mouseHandler = new MouseHandler();
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    /**
     * O método mais importante: desenha o jogo.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Limpa o painel

        // 1. Manda a Matriz desenhar tudo que é permanente
        // (Grade, Bolas, Traços completos)
        matriz.desenhar(g);

        // 2. Desenha o traço que o usuário está fazendo AGORA
        // (por cima de tudo, exceto das bolas)
        if (tracoAtual != null) {
            tracoAtual.desenhar(g, TAMANHO_CELULA);
        }
    }

    /**
     * Converte uma coordenada de pixel (ex: 450px) para uma
     * coordenada da grade (ex: célula 6).
     */
    private Point converterPixelParaGrade(int x, int y) {
        int coluna = x / TAMANHO_CELULA;
        int linha = y / TAMANHO_CELULA;
        return new Point(coluna, linha); // Point(x=coluna, y=linha)
    }

    /**
     * Classe interna para lidar com todos os eventos do mouse.
     */
    private class MouseHandler extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            // 1. Descobrir em qual célula o usuário clicou
            Point p = converterPixelParaGrade(e.getX(), e.getY());
            int lin = p.y;
            int col = p.x;

            // 2. O que tem nessa célula?
            int estado = matriz.getEstado(lin, col);

            // 3. Se for uma Bola (estado > 0 e < 100)
            if (estado > 0 && estado < 100) {
                // (Lógica futura: se a bola já estiver conectada, apagar o traço antigo)

                // 4. Pega o objeto Bola
                // **** CORREÇÃO FEITA AQUI ****
                Bolas bolaClicada = matriz.getBolaEm(lin, col); //
                
                if (bolaClicada != null) {
                    // 5. Começa um NOVO traço!
                    tracoAtual = new Traco(
                        bolaClicada.getCorID(),
                        bolaClicada.getCorGrafica(),
                        lin, col
                    );
                    // 6. Marca na matriz que estamos começando um caminho
                    matriz.setEstado(lin, col, bolaClicada.getCorID() + 100);
                    repaint();
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            // Só faz algo se estivermos de fato desenhando um traço
            if (tracoAtual == null) {
                return;
            }

            // 1. Descobre a célula atual do mouse
            Point p = converterPixelParaGrade(e.getX(), e.getY());
            int lin = p.y;
            int col = p.x;

            // 2. Pega o último ponto que adicionamos ao traço
            Point ultimoPonto = tracoAtual.getUltimoPonto();
            
            // 3. Se o mouse ainda está na mesma célula, não faz nada
            if (ultimoPonto.x == col && ultimoPonto.y == lin) {
                return;
            }

            // 4. Verifica se a nova posição é válida
            if (!matriz.isPosicaoValida(lin, col)) {
                // Usuário arrastou para fora da tela. Cancela o traço.
                apagarTracoAtual();
                repaint();
                return;
            }

            // 5. O que tem na nova célula?
            int estado = matriz.getEstado(lin, col);

            // --- LÓGICA PRINCIPAL ---

            if (estado == 0) { // CASO 1: CÉLULA VAZIA
                // Movimento válido. Adiciona o ponto ao traço.
                tracoAtual.adicionarPonto(lin, col);
                // Marca na matriz o caminho
                matriz.setEstado(lin, col, tracoAtual.getCorID() + 100);

            } else if (estado == tracoAtual.getCorID()) { // CASO 2: BOLA DA MESMA COR!
                // SUCESSO! Completou o caminho.
                tracoAtual.adicionarPonto(lin, col);
                tracoAtual.setEstaCompleto(true);
                matriz.adicionarTracoCompleto(tracoAtual); // Move para a lista de completos
                tracoAtual = null; // Para de desenhar
                
                // (Lógica de verificar vitória)
                // if (matriz.verificarVitoria()) { ... }

            } else { // CASO 3: COLISÃO!
                // Bateu em outra cor ou em si mesmo (se não for backtracking)
                // Cancela o traço atual.
                apagarTracoAtual();
            }

            // 6. Redesenha a tela
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // Se o usuário soltar o mouse e o traço NÃO estiver completo
            if (tracoAtual != null && !tracoAtual.isEstaCompleto()) {
                // Cancela o traço
                apagarTracoAtual();
                repaint();
            }
        }
    } // Fim da classe MouseHandler

    /**
     * Um método de ajuda para apagar o traço que está sendo desenhado.
     * Isso limpa o traço da matriz lógica e o remove.
     */
    // **** CORREÇÃO FEITA AQUI ****
    private void apagarTracoAtual() {
        if (tracoAtual == null) return;
        
        // Chama o método da Matriz (que você deve adicionar)
        matriz.limparTracoDaGrade(tracoAtual); 

        // Esquece o traço
        tracoAtual = null;
    }
}