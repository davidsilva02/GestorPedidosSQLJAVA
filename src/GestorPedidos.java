import java.sql.*;
import java.util.Scanner;


public class GestorPedidos {
    Connection c=null;
    Statement st=null;
    Scanner sc;
    public static void main(String[] args) {
        GestorPedidos g= new GestorPedidos();
        g.programa();
    }

    public GestorPedidos() {
        sc=new Scanner(System.in);
    }
    private void programa() {
        c= new ConnectSQL().connect();

        int option=-1;
        while (option!=0){
            System.out.println("OPCA0 1 - VER PEDIDOS");
            System.out.println("OPCAO 2 - CRIAR PEDIDO");
            System.out.println("OPCAO 3 - ELIMINAR PEDIDO");
            System.out.println("OPCAO 0 - SAIR");

            option=sc.nextInt();

            if (option==1) verPedidos();
            if (option==2) inputCriarPedido();
//            if (option==3) eliminarPedido();
            }
    }
//    private void eliminarPedido() {
//        verPedidos();
//        System.out.println("PEDIDO A ELIMINAR: ");
//        int id=sc.nextInt();
//        try {
//            st=c.createStatement();
//            st.executeUpdate("delete from pedidos where id_pedido= " + id + ";");
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public void eliminarPedido(String idPedidoSelect){
        int idPedido=Integer.parseInt(idPedidoSelect);
        try {
            c= new ConnectSQL().connect();
            st=c.createStatement();
            st.executeUpdate("delete from pedidos where id_pedido= " + idPedido + ";");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void inputCriarPedido(){
        System.out.println("CRIAR PEDIDO");
        String assunto_pedido="";
        String descricao_pedido="";
        boolean control;

        do {
            System.out.println("----------------------------------------");
            System.out.println("Introduza o assunto do pedido: ");
            assunto_pedido=sc.nextLine();
            System.out.println(assunto_pedido);
            System.out.println("Introduza a descricao do pedido");
            descricao_pedido=sc.nextLine();
            System.out.println(descricao_pedido);
            control=criarPedido(assunto_pedido,descricao_pedido);
        }
        while (!control);
        
    

    }
    public boolean criarPedido(String assuntoPedido,String descricaoPedido) {
        if(assuntoPedido.length()>80 || assuntoPedido.length()==0 ||descricaoPedido.length()>300 || descricaoPedido.length()==0 ){
            return false;
        }

        else{
            //System.out.println(assuntoPedido);
            //System.out.println(descricaoPedido);
            try {
                c= new ConnectSQL().connect();
                st=c.createStatement();
                st.executeUpdate("insert into pedidos (assunto_pedido, descricao_pedido) values ('"+assuntoPedido+"','"+descricaoPedido+"');");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    private void verPedidos(){
        try {
            st=c.createStatement();
            ResultSet results = st.executeQuery("select * from pedidos;");
            while(results.next()){
                System.out.printf("PEDIDO NUMERO %d\n", results.getInt(1));
                System.out.println("----------------------------------------------------------------------------------------------------");
                System.out.println(results.getString(2));
                System.out.println(results.getString(3));
            }
            System.out.println("\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
