import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Matriz é a classe "cérebro" do jogo.
 * Armazena a grade lógica e os objetos do jogo (Bolas, Traços).
 */
public class Matriz {

    // --- 1. Atributos ---
    private int[][] gradeLogica;
    private int linhas;
    private int colunas;
    private int tamanhoCelula;

    private List<Bolas> listaDeBolas;
    private List<Traco> tracosCompletos;

    // --- 2. Métodos ---

    /**
     * Constrói uma nova Matriz com as dimensões especificadas.
     */
    public Matriz(int linhas, int colunas, int tamanhoCelula) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.tamanhoCelula = tamanhoCelula;

        this.gradeLogica = new int[linhas][colunas];
        this.listaDeBolas = new ArrayList<>();
        this.tracosCompletos = new ArrayList<>();
    }

    /**
     * Limpa completamente a matriz para carregar um nível.
     */
    public void limparTudo() {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                gradeLogica[i][j] = 0; // 0 = Vazio
            }
        }
        listaDeBolas.clear();
        tracosCompletos.clear();
    }

    /**
     * **** MÉTODO RESTAURADO ****
     * Carrega um nível de exemplo fixo (hardcoded).
     * Como não há GerenciadorNiveis, os dados do nível ficam aqui.
     */
    public void carregarNivelExemplo() {
        // 1. Limpa o tabuleiro
        limparTudo();
        
        // 2. Adiciona as bolas do nível
        // Você pode alterar este nível como quiser
        
        // Par Vermelho (ID 1)
        adicionarBola(new Bolas(0, 5, 1, Color.RED));
        adicionarBola(new Bolas(8, 5, 1, Color.RED));
        
        // Par Azul (ID 2)
        adicionarBola(new Bolas(5, 0, 2, Color.BLUE));
        adicionarBola(new Bolas(5, 8, 2, Color.BLUE));
        

    }
    
    
    /**
     * Helper privado para adicionar uma bola e atualizar a grade.
     */
    private void adicionarBola(Bolas bola) {
        listaDeBolas.add(bola);
        gradeLogica[bola.getLinha()][bola.getColuna()] = bola.getCorID();
    }

    /**
     * Desenha todos os componentes visuais gerenciados pela Matriz.
     */
    public void desenhar(Graphics g) {
        // 1. Desenhar linhas da grade
        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i <= linhas; i++) {
            g.drawLine(0, i * tamanhoCelula, colunas * tamanhoCelula, i * tamanhoCelula);
        }
        for (int j = 0; j <= colunas; j++) {
            g.drawLine(j * tamanhoCelula, 0, j * tamanhoCelula, linhas * tamanhoCelula);
        }

        // 2. Desenhar os traços completos
        for (Traco traco : tracosCompletos) {
            traco.desenhar(g, tamanhoCelula);
        }
        
        // 3. Desenhar as bolas
        for (Bolas bola : listaDeBolas) {
            bola.desenhar(g, tamanhoCelula);
        }
    }
    
    // --- 3. Métodos de Acesso e Ajuda ---
    
    public int getEstado(int linha, int coluna) {
        if (!isPosicaoValida(linha, coluna)) {
            return -1;
        }
        return gradeLogica[linha][coluna];
    }
    
    public void setEstado(int linha, int coluna, int estado) {
        if (isPosicaoValida(linha, coluna)) {
            gradeLogica[linha][coluna] = estado;
        }
    }
    
    public Bolas getBolaEm(int linha, int coluna) {
        if (!isPosicaoValida(linha, coluna)) {
            return null;
        }
        for (Bolas bola : listaDeBolas) {
            if (bola.getLinha() == linha && bola.getColuna() == coluna) {
                return bola;
            }
        }
        return null;
    }

    public boolean isPosicaoValida(int linha, int coluna) {
        return linha >= 0 && linha < linhas && coluna >= 0 && coluna < colunas;
    }
    
    public void adicionarTracoCompleto(Traco traco) {
        tracosCompletos.add(traco);
        
        Point pInicial = traco.getPontosDoCaminho().get(0);
        Point pFinal = traco.getUltimoPonto();

        Bolas bola1 = getBolaEm(pInicial.y, pInicial.x);
        Bolas bola2 = getBolaEm(pFinal.y, pFinal.x);

        if (bola1 != null) {
            bola1.setEstaConectada(true);
        }
        if (bola2 != null) {
            bola2.setEstaConectada(true);
        }
    }
    
    public void limparTracoDaGrade(Traco traco) {
        if (traco == null) return;
        
        List<Point> pontos = traco.getPontosDoCaminho();
        
        for (Point p : pontos) {
            Bolas bola = getBolaEm(p.y, p.x);
            
            if (bola != null) {
                gradeLogica[p.y][p.x] = bola.getCorID();
            } else {
                gradeLogica[p.y][p.x] = 0;
            }
        }
    }
}