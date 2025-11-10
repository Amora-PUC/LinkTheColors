import java.awt.Color;
import java.awt.Graphics;
/**
 * Escreva uma descrição da classe Bolas aqui.
 * 
 * @author (seu nome) 
 * @version (um número da versão ou uma data)
 */
public class Bolas{
    //posição na grade 
    private int linha;
    private int coluna;

    // Cor Lógica (ex: 1 = Vermelho, 2 = Azul, etc.)
    private int corID;

    //cor grafica
    private Color corGrafica;

    private boolean estaConectada;

    /**
     * Construtor para criar uma nova Bola.
     */
    public Bolas(int linha, int coluna, int corID, Color corGrafica){
        this.linha = linha;
        this.coluna = coluna;
        this.corID = corID;
        this.corGrafica = corGrafica;
        this.estaConectada = false;
    }

    /**
     * Método para a bola se desenhar na tela.
     * @param g O contexto gráfico onde desenhar (vem da sua tela de jogo)
     * @param tamanhoCelula O tamanho em pixels de cada célula da grade
     */
    public void desenhar(Graphics g, int tamanhoCelula){
        // Calcular a posição em pixels (x, y) a partir da grade (linha, coluna)
        // Damos uma pequena margem para a bola não ficar colada na borda
        int margem = (int)(tamanhoCelula *0.1);// Margem de 10%
        int x_pixel = (this.coluna * tamanhoCelula) + margem;
        int y_pixel = (this.linha * tamanhoCelula) + margem;

        // 2. Calcular o diâmetro da bola
        int diametro = (int)(tamanhoCelula * 0.8);// Bola ocupa 80% da célula

        // 3. Definir a cor
        g.setColor(this.corGrafica);

        // 4. Desenhar o círculo preenchido
        g.fillOval(x_pixel, y_pixel, diametro, diametro);

        //Se estiver conectada, desenha uma borda diferente
        if (this.estaConectada) {
            g.setColor(Color.WHITE); 
            g.drawOval(x_pixel, y_pixel, diametro, diametro);
        }
    }

    // --- Getters e Setters ---

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    public int getCorID() {
        return corID;
    }

    public Color getCorGrafica() {
        return corGrafica;
    }

    public boolean isEstaConectada() {
        return estaConectada;
    }

    public void setEstaConectada(boolean estaConectada) {
        this.estaConectada = estaConectada;
    }

}
