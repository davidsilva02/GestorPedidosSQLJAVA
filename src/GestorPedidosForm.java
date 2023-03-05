import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class GestorPedidosForm {

    static JFrame janela;
    JPanel painelCriarPedido, painelVerPedidos ;
    JLabel aPLabel, dPLabel,verPedidosLabel;
    JTextArea aPArea, dPArea,verPedidosArea;
    JButton cPBtn, dPBtn, vpPBtn;
    public String numeroPedidoSelect, assuntoPedidoSelect, descricaoPedidoSelect;
    JTable table;
    ArrayList<Pedido> pedidos;
    DefaultTableModel tM;

    public static void main(String[] args) {
        GestorPedidosForm gF=new GestorPedidosForm();

        janela = new JFrame();
        janela.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        janela.setVisible(true);
        janela.setSize(600, 545);

        gF.start();
    }

    public void start(){
        painelCriarPedido();
    }



    public void painelCriarPedido(){

        //Criacao painel criar pedido
        painelCriarPedido = new JPanel();
        painelCriarPedido.setVisible(true);
        painelCriarPedido.setLayout(null);
        janela.add(painelCriarPedido);

        //Painel Criar Pedido

        // Criacao label assunto pedido
        aPLabel = new JLabel();
        aPLabel.setText("Assunto Pedido");
        aPLabel.setBounds(5, 5, 515, 40);
        aPLabel.setFont(new Font("Arial", Font.BOLD, 40));
        painelCriarPedido.add(aPLabel);

        //Criacao text field assunto pedido
        aPArea = new JTextArea();
        aPArea.setBackground(new Color(219, 219, 219));
        aPArea.setLineWrap(true);
        aPArea.setBounds(5, 50, 515, 40);
        painelCriarPedido.add(aPArea);

        // Criacao label descricao pedido
        dPLabel = new JLabel();
        dPLabel.setText("Descricao Pedido");
        dPLabel.setBounds(5, 100, 515, 40);
        dPLabel.setFont(new Font("Arial", Font.BOLD, 40));
        painelCriarPedido.add(dPLabel);


        //Criacao text field descricao pedido
        dPArea = new JTextArea();
        dPArea.setBackground(new Color(219, 219, 219));
        dPArea.setLineWrap(true);
        dPArea.setBounds(5, 150, 515, 300);
        painelCriarPedido.add(dPArea);

        //Criacao btn CriarPedido
        cPBtn = new JButton();
        cPBtn.setText("Criar pedido");
        cPBtn.setBounds(5, 460, 160, 30);
        painelCriarPedido.add(cPBtn);
        cPBtn.addActionListener(e -> {
            if (!painelCriarPedido.isVisible()) {
                painelVerPedidos.setVisible(false);
                painelCriarPedido();
            }

            else {
                if(new GestorPedidos().criarPedido(aPArea.getText(),dPArea.getText())){
                    aPArea.setText("");
                    dPArea.setText("");
                }
                else{
                    JOptionPane.showMessageDialog(janela,"VALORES INCORRETOS");
                    aPArea.setText("");
                    dPArea.setText("");
                }
            }
        });

        //Criacao btn Descartar
        dPBtn = new JButton();
        dPBtn.setText("Descartar pedido");
        dPBtn.setBounds(175, 460, 160, 30);
        painelCriarPedido.add(dPBtn);

        dPBtn.addActionListener(e -> {
            if (painelCriarPedido.isVisible()){
                String text="Deseja descartar o pedido?" ;
                int option=JOptionPane.showConfirmDialog(painelVerPedidos, text,text, JOptionPane.YES_NO_OPTION);
                if (option==JOptionPane.YES_OPTION){
                    aPArea.setText("");
                    dPArea.setText("");
                }
            }

            if (painelVerPedidos.isVisible() && numeroPedidoSelect!=null){
                String text="Deseja eliminar o pedido " + numeroPedidoSelect + "?" ;
                int option=JOptionPane.showConfirmDialog(painelVerPedidos, text,text, JOptionPane.YES_NO_OPTION);
                if (option==JOptionPane.YES_OPTION){
                     new GestorPedidos().eliminarPedido(numeroPedidoSelect);
                    atualizarPedidosTabela(numeroPedidoSelect);
                    textPedidosSelect();
                }
            }
        });


        //Criacao btn VerPedidos
        vpPBtn = new JButton();
        vpPBtn.setText("Ver pedidos");
        vpPBtn.setBounds(350, 460, 160, 30);
        painelCriarPedido.add(vpPBtn);
        vpPBtn.addActionListener(e -> {
            if (painelCriarPedido.isVisible()) {
                painelCriarPedido.setVisible(false);
                painelVerPedidos();
            }
        });
    }

    public void painelVerPedidos(){
        //Criacao painel ver pedidos
        painelVerPedidos = new JPanel();
        painelVerPedidos.setVisible(true);
        painelVerPedidos.setLayout(null);
        janela.add(painelVerPedidos);


        //Criacao LabelVerPedidos
        verPedidosLabel=new JLabel();
        verPedidosLabel.setText("Ver Pedidos");

        verPedidosLabel.setBounds(5, 5, 515, 40);
        verPedidosLabel.setFont(new Font("Arial", Font.BOLD, 40));

        painelVerPedidos.add(verPedidosLabel);
        
        painelVerPedidos.add(cPBtn);

        mostrarPedidosTabela();

        verPedidosArea=new JTextArea();
        verPedidosArea.setBounds(5,255,525,200);
        verPedidosArea.setBackground(painelVerPedidos.getBackground());
        verPedidosArea.setBorder(BorderFactory.createLineBorder(Color.black));
        verPedidosArea.setEditable(false);
        verPedidosArea.setLineWrap(true);
        textPedidosSelect();
        painelVerPedidos.add(verPedidosArea);

        dPBtn.setText("Apagar Pedido");
        painelVerPedidos.add(dPBtn);
    }

    public void textPedidosSelect(){
        String text;
        if(pedidos.size()==0){
             text="Não existem pedidos";
             verPedidosArea.setText(text);
             verPedidosArea.setForeground(Color.red);
             verPedidosArea.setFont(new Font("Arial",Font.BOLD,20));
        }
        else{
             text="Assunto Pedido" + "\n" + "Descricao Pedido";
             verPedidosArea.setText(text);
        }
    }

    public ArrayList<Pedido> mostrarPedidosList(){
        Connection c= new ConnectSQL().connect();
        ArrayList<Pedido> pedidos=new ArrayList<>();
        Pedido pedido;
        try {
            Statement st=c.createStatement();
            ResultSet results = st.executeQuery("select * from pedidos;");
            while(results.next()){
                pedido=new Pedido(results.getInt(1),results.getString(2),results.getString(3));
                pedidos.add(pedido);
            }

            return pedidos;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void atualizarPedidosTabela(String numeroPedidoSelect_){
        table.setVisible(false);
        int numeroPedido=Integer.parseInt(numeroPedidoSelect_);

        pedidos.removeIf(ped -> ped.getNumeroPedido() == numeroPedido);

        tM=new DefaultTableModel(){
            @Override
            public int getRowCount() {
                return pedidos.size();
            }

            @Override
            public int getColumnCount() {
                return 3;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Pedido pedido=pedidos.get(rowIndex);
                return switch (columnIndex) {
                    case 0 -> pedido.getNumeroPedido();
                    case 1 -> pedido.getAssuntoPedido();
                    case 2 -> pedido.getDescricaoPedido();
                    default -> null;
                };
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };


        JTable table_up=new JTable(tM);
        table_up.setBounds(5,60,525,190);
        table_up.setBackground(painelVerPedidos.getBackground());
        table_up.setFont(new Font("Arial",Font.PLAIN,13));
        table_up.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            int row;
            public void valueChanged(ListSelectionEvent e) {
                if ((row=table_up.getSelectedRow())>-1){
                    numeroPedidoSelect=table_up.getValueAt(row,0).toString();
                    assuntoPedidoSelect=table_up.getValueAt(row,1).toString();
                    descricaoPedidoSelect=table_up.getValueAt(row,2).toString();

                    String textArea="Numero pedido: " + numeroPedidoSelect + "\n" +
                            "Assunto Pedido: \n" + assuntoPedidoSelect + "\n" +
                            "Descricao Pedido: \n" + descricaoPedidoSelect;

                    verPedidosArea.setText(textArea);
                }
            }
        });
        painelVerPedidos.add(table_up);
    }
    public void mostrarPedidosTabela(){
        pedidos=mostrarPedidosList();
        Object [] colums={"N","Assunto Pedido","Descricao Pedido"};
        tM=new DefaultTableModel(){
            @Override
            public int getRowCount() {
                return pedidos.size();
            }

            @Override
            public int getColumnCount() {
                return 3;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Pedido pedido=pedidos.get(rowIndex);
                return switch (columnIndex) {
                    case 0 -> pedido.getNumeroPedido();
                    case 1 -> pedido.getAssuntoPedido();
                    case 2 -> pedido.getDescricaoPedido();
                    default -> null;
                };
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        tM.setColumnIdentifiers(colums);
        table=new JTable(tM);
        table.setBounds(5,60,525,190);
        table.setBackground(painelVerPedidos.getBackground());
        table.setFont(new Font("Arial",Font.PLAIN,13));
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            int row;
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if ((row=table.getSelectedRow())>-1){
                    numeroPedidoSelect=table.getValueAt(row,0).toString();
                    assuntoPedidoSelect=table.getValueAt(row,1).toString();
                    descricaoPedidoSelect=table.getValueAt(row,2).toString();

                    String textArea="Numero pedido: " + numeroPedidoSelect + "\n" +
                                    "Assunto Pedido: \n" + assuntoPedidoSelect + "\n" +
                                    "Descricao Pedido: \n" + descricaoPedidoSelect;

                    verPedidosArea.setText(textArea);
                }
            }
        });
        table.getSelectionModel().setSelectionInterval(table.getSelectedRow(), table.getSelectedRow());
        table.scrollRectToVisible(new Rectangle(table.getCellRect(table.getSelectedRow(),0,true)));
        painelVerPedidos.add(table);
    }
}


