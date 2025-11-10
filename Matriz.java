import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point; // Importar a classe Point
import java.util.ArrayList;
import java.util.List;

public class Matriz {

    // --- 1. Atributos ---
    private int[][] gradeLogica;
    private int linhas;
    private int colunas;
    private int tamanhoCelula; // Em pixels

    private List<Bolas> listaDeBolas; //
    private List<Traco> tracosCompletos;

    // --- 2. Métodos ---

    /**
     * Construtor
     */
    public Matriz(int linhas, int colunas, int tamanhoCelula) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.tamanhoCelula = tamanhoCelula;

        this.gradeLogica = new int[linhas][colunas]; // Inicia com 0 (vazio)
        this.listaDeBolas = new ArrayList<>(); //
        this.tracosCompletos = new ArrayList<>();
    }
    
    /**
     * Limpa a grade e as listas para carregar um novo nível.
     */
    public void limparTudo() {
        // Zera a grade lógica
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                gradeLogica[i][j] = 0; // 0 = Vazio
            }
        }
        // Limpa as listas de objetos
        listaDeBolas.clear(); //
        tracosCompletos.clear();
    }

    /**
     * Carrega um nível (exemplo simples).
     * Na prática, você leria isso de um arquivo ou um objeto "Nivel".
     */
    public void carregarNivelExemplo() {
        limparTudo();
        
        // Par Vermelho (ID 1)
        adicionarBola(new Bolas(0, 1, 1, Color.RED)); //
        adicionarBola(new Bolas(4, 3, 1, Color.RED)); //
        
        // Par Azul (ID 2)
        adicionarBola(new Bolas(1, 1, 2, Color.BLUE)); //
        adicionarBola(new Bolas(3, 4, 2, Color.BLUE)); //
    }
    
    // Método privado para ajudar a carregar o nível
    private void adicionarBola(Bolas bola) { //
        listaDeBolas.add(bola); //
        // MUITO IMPORTANTE: Atualiza a grade lógica
        gradeLogica[bola.getLinha()][bola.getColuna()] = bola.getCorID(); //
    }


    /**
     * Desenha todos os componentes permanentes da matriz.
     */
    public void desenhar(Graphics g) {
        // 1. Desenhar linhas da grade (opcional, mas bom para debug)
        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i <= linhas; i++) {
            g.drawLine(0, i * tamanhoCelula, colunas * tamanhoCelula, i * tamanhoCelula);
        }
        for (int j = 0; j <= colunas; j++) {
            g.drawLine(j * tamanhoCelula, 0, j * tamanhoCelula, linhas * tamanhoCelula);
        }

        // 2. Desenhar os traços completos
        for (Traco traco : tracosCompletos) {
            traco.desenhar(g, tamanhoCelula); //
        }
        
        // 3. Desenhar as bolas (por cima dos traços)
        for (Bolas bola : listaDeBolas) { //
            bola.desenhar(g, tamanhoCelula); //
        }
    }
    
    // --- 3. Métodos de Acesso e Ajuda ---
    
    public int getEstado(int linha, int coluna) {
        if (!isPosicaoValida(linha, coluna)) {
            return -1; // Posição inválida (código de erro)
        }
        return gradeLogica[linha][coluna];
    }
    
    public void setEstado(int linha, int coluna, int estado) {
        if (isPosicaoValida(linha, coluna)) {
            gradeLogica[linha][coluna] = estado;
        }
    }
    
    public Bolas getBolaEm(int linha, int coluna) { //
        if (!isPosicaoValida(linha, coluna)) {
            return null;
        }
        // Procura na lista de bolas
        for (Bolas bola : listaDeBolas) { //
            if (bola.getLinha() == linha && bola.getColuna() == coluna) { //
                return bola; //
            }
        }
        return null; // Nenhuma bola encontrada
    }

    public boolean isPosicaoValida(int linha, int coluna) {
        return linha >= 0 && linha < linhas && coluna >= 0 && coluna < colunas;
    }
    
    /**
     * Adiciona um traço completo e atualiza o estado das bolas conectadas.
     * **** MÉTODO ATUALIZADO ****
     */
    public void adicionarTracoCompleto(Traco traco) {
        tracosCompletos.add(traco); //
        
        // Pega o ponto inicial e final do traço
        Point pInicial = traco.getPontosDoCaminho().get(0); //
        Point pFinal = traco.getUltimoPonto(); //

        // Encontra as bolas nessas posições e as marca como conectadas
        Bolas bola1 = getBolaEm(pInicial.y, pInicial.x); //
        Bolas bola2 = getBolaEm(pFinal.y, pFinal.x); //

        if (bola1 != null) {
            bola1.setEstaConectada(true); //
        }
        if (bola2 != null) {
            bola2.setEstaConectada(true); //
        }
    }
    
    /**
     * Limpa um traço da grade lógica, resetando as células para 0 (vazio)
     * ou para o ID da bola.
     * **** MÉTODO NOVO ****
     */
    public void limparTracoDaGrade(Traco traco) {
        if (traco == null) return;
        
        List<Point> pontos = traco.getPontosDoCaminho(); //
        
        // Itera por todos os pontos do caminho
        for (Point p : pontos) {
            // Verifica se é uma bola
            Bolas bola = getBolaEm(p.y, p.x); //
            
            if (bola != null) {
                // Se for uma bola, reseta o estado para o ID da cor
                gradeLogica[p.y][p.x] = bola.getCorID(); //
            } else {
                // Se for um caminho, reseta para VAZIO
                gradeLogica[p.y][p.x] = 0; 
            }
        }
    }
    
}