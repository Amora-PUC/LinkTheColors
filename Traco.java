import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Traco {

    // --- 1. Atributos ---

    // ID da cor (1=Vermelho, 2=Azul, etc.)
    private int corID;
    // Cor para desenhar
    private Color corGrafica;
    // Lista de células (linha, coluna) por onde o traço passa
    // Usaremos Point, onde point.y = linha e point.x = coluna
    private List<Point> caminho;
    
    private boolean estaCompleto;

    // --- 2. Métodos ---

    /**
     * Construtor para criar um novo Traço.
     * Começa com o ponto inicial (a posição da bola).
     */
    public Traco(int corID, Color corGrafica, int linhaInicial, int colunaInicial) {
        this.corID = corID;
        this.corGrafica = corGrafica;
        this.caminho = new ArrayList<>();
        this.estaCompleto = false;
        
        // Adiciona o primeiro ponto
        adicionarPonto(linhaInicial, colunaInicial);
    }

    /**
     * Adiciona uma nova célula ao caminho do traço.
     */
    public void adicionarPonto(int linha, int coluna) {
        this.caminho.add(new Point(coluna, linha)); // Lembre-se: x=coluna, y=linha
    }

    /**
     * Remove e retorna o último ponto do caminho (para backtracking).
     * @return O ponto que foi removido.
     */
    public Point removerUltimoPonto() {
        if (caminho.size() > 1) { // Nunca remove o ponto inicial
            return caminho.remove(caminho.size() - 1);
        }
        return getUltimoPonto(); // Retorna o último ponto se só houver 1
    }

    /**
     * Pega o último ponto adicionado ao caminho.
     */
    public Point getUltimoPonto() {
        if (caminho.isEmpty()) {
            return null;
        }
        return caminho.get(caminho.size() - 1);
    }
    
    /**
     * Verifica se um ponto (célula) já está neste traço.
     */
    public boolean contemPonto(int linha, int coluna) {
        // new Point(coluna, linha)
        return caminho.contains(new Point(coluna, linha));
    }

    /**
     * Método para o traço se desenhar na tela.
     * @param g O contexto gráfico
     * @param tamanhoCelula O tamanho em pixels de cada célula
     */
    public void desenhar(Graphics g, int tamanhoCelula) {
        // Para linhas grossas, precisamos do Graphics2D
        Graphics2D g2d = (Graphics2D) g;
        
        // Define a cor e a espessura da linha (ex: 30% da célula)
        g2d.setColor(this.corGrafica);
        int espessura = (int) (tamanhoCelula * 0.3); // Linha grossa
        // CAP_ROUND e JOIN_ROUND deixam as pontas e cantos arredondados
        g2d.setStroke(new BasicStroke(espessura, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        // Metade do tamanho da célula, para achar o centro
        int centro = tamanhoCelula / 2;

        // Desenha linhas de um ponto para o próximo
        for (int i = 0; i < caminho.size() - 1; i++) {
            Point p1 = caminho.get(i); // Ponto atual
            Point p2 = caminho.get(i + 1); // Próximo ponto

            // Converte coordenadas da grade (linha, coluna) para pixels (x, y)
            // Lembre-se: p1.x é a COLUNA, p1.y é a LINHA
            int x1 = (p1.x * tamanhoCelula) + centro;
            int y1 = (p1.y * tamanhoCelula) + centro;
            int x2 = (p2.x * tamanhoCelula) + centro;
            int y2 = (p2.y * tamanhoCelula) + centro;
            
            // Desenha a linha entre os centros das células
            g2d.drawLine(x1, y1, x2, y2);
        }
    }

    // --- 3. Getters e Setters ---

    public int getCorID() {
        return corID;
    }
    
    public List<Point> getPontosDoCaminho() {
        return caminho;
    }

    public boolean isEstaCompleto() {
        return estaCompleto;
    }

    public void setEstaCompleto(boolean estaCompleto) {
        this.estaCompleto = estaCompleto;
    }
}