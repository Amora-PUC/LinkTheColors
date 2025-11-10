import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

/**
 * PainelDoJogo é o componente Swing principal onde o jogo é renderizado e jogado.
 */
public class PainelDoJogo extends JPanel {

    // --- Constantes de Configuração ---
    public static final int LINHAS = 9;
    public static final int COLUNAS = 10;
    public static final int TAMANHO_CELULA = 70;

    // --- Atributos de Estado ---
    private Matriz matriz;
    private Traco tracoAtual; 
    
    // A variável 'nivelAtual' foi REMOVIDA

    /**
     * Constrói um novo PainelDoJogo.
     * **** MUDANÇA: O construtor não recebe mais o número do nível ****
     */
    public PainelDoJogo() {
        // 1. Configurar as dimensões e o fundo
        int largura = COLUNAS * TAMANHO_CELULA;
        int altura = LINHAS * TAMANHO_CELULA;
        setPreferredSize(new Dimension(largura, altura));
        setBackground(new Color(45, 45, 55));

        // 2. Inicializar a Matriz
        this.matriz = new Matriz(LINHAS, COLUNAS, TAMANHO_CELULA);
        
        // 3. Carregar o nível
        // **** MUDANÇA: Chama o método sem argumentos ****
        this.matriz.carregarNivelExemplo(); 

        // 4. Inicializar o traço
        this.tracoAtual = null;

        // 5. Configurar os "ouvintes" de mouse
        MouseHandler mouseHandler = new MouseHandler();
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }
    
    /**
     * Reseta o nível atual para seu estado inicial.
     * Chamado pelo botão "Limpar".
     */
    public void resetarNivel() {
        // 1. Recarrega o nível
        // **** MUDANÇA: Chama o método sem argumentos ****
        matriz.carregarNivelExemplo(); 
        
        // 2. Cancela qualquer traço sendo desenhado
        apagarTracoAtual(); 
        
        // 3. Redesenha o painel
        repaint();
    }

    /**
     * Sobrescreve o método principal de desenho do JPanel.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        matriz.desenhar(g);
        if (tracoAtual != null) {
            tracoAtual.desenhar(g, TAMANHO_CELULA);
        }
    }

    /**
     * Converte coordenadas de pixel para grade.
     */
    private Point converterPixelParaGrade(int x, int y) {
        int coluna = x / TAMANHO_CELULA;
        int linha = y / TAMANHO_CELULA;
        return new Point(coluna, linha);
    }

    /**
     * Helper privado para cancelar o traço atual.
     */
    private void apagarTracoAtual() {
        if (tracoAtual == null) return;
        matriz.limparTracoDaGrade(tracoAtual); 
        tracoAtual = null;
    }

    /**
     * Classe interna que gerencia todos os eventos de mouse.
     */
    private class MouseHandler extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            Point p = converterPixelParaGrade(e.getX(), e.getY());
            int lin = p.y;
            int col = p.x;
            int estado = matriz.getEstado(lin, col);

            if (estado > 0 && estado < 100) {
                Bolas bolaClicada = matriz.getBolaEm(lin, col);
                if (bolaClicada != null) {
                    tracoAtual = new Traco(
                        bolaClicada.getCorID(),
                        bolaClicada.getCorGrafica(),
                        lin, col
                    );
                    matriz.setEstado(lin, col, bolaClicada.getCorID() + 100);
                    repaint();
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (tracoAtual == null) return;

            Point p = converterPixelParaGrade(e.getX(), e.getY());
            int lin = p.y;
            int col = p.x;
            Point ultimoPonto = tracoAtual.getUltimoPonto();
            
            if (ultimoPonto.x == col && ultimoPonto.y == lin) return;
            if (!matriz.isPosicaoValida(lin, col)) {
                apagarTracoAtual();
                repaint();
                return;
            }

            int estado = matriz.getEstado(lin, col);

            if (estado == 0) { 
                tracoAtual.adicionarPonto(lin, col);
                matriz.setEstado(lin, col, tracoAtual.getCorID() + 100);
            } else if (estado == tracoAtual.getCorID()) {
                tracoAtual.adicionarPonto(lin, col);
                tracoAtual.setEstaCompleto(true);
                matriz.adicionarTracoCompleto(tracoAtual);
                tracoAtual = null;
            } else {
                apagarTracoAtual();
            }
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (tracoAtual != null && !tracoAtual.isEstaCompleto()) {
                apagarTracoAtual();
                repaint();
            }
        }
    } // Fim da classe MouseHandler
}