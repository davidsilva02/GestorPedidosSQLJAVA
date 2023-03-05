public class Pedido {
    private int numeroPedido;
    private String assuntoPedido;
    private String descricaoPedido;

    public Pedido(int numeroPedido, String assuntoPedido, String descricaoPedido) {
        this.numeroPedido = numeroPedido;
        this.assuntoPedido = assuntoPedido;
        this.descricaoPedido = descricaoPedido;
    }

    public int getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(int numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public String getAssuntoPedido() {
        return assuntoPedido;
    }

    public void setAssuntoPedido(String assuntoPedido) {
        this.assuntoPedido = assuntoPedido;
    }

    public String getDescricaoPedido() {
        return descricaoPedido;
    }

    public void setDescricaoPedido(String descricaoPedido) {
        this.descricaoPedido = descricaoPedido;
    }
}
