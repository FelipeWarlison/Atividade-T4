/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import bdados.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.openide.util.Exceptions;



/**
 *
 * @author usuario
 */
public class Consulta {

    private Conexao Conexao;
    private Statement Comando;
    private PreparedStatement comandoPreparado;
    private ResultSet Resultado;
        
    public Consulta()
    {

        try
        {
            Conexao = new Conexao();
            Comando = Conexao.conexao.createStatement();
            String Selecao = "";
            //Selecao = "SELECT MC.\"MB_CODIGO\", MB.\"MB_IMPLEMENTACAO\"  FROM \"METODO_BRUTO\" MB, \"METODO_CARACTERISTICA\" MC  WHERE (MB.\"MB_CODIGO\"=1) OR (MC.\"BG_CODIGO\"=1) OR (MB.\"MB_CODIGO\"=MC.\"MB_CODIGO\") OR (MB.\"MB_CODIGO\"<>0) OR (MB.\"MB_CODIGO\"<>0) ";
            //Selecao = "SELECT MB.\"MB_CODIGO\", MB.\"MB_IMPLEMENTACAO\" FROM \"METODO_BRUTO\" MB WHERE MB.\"MB_CODIGO\" NOT IN (SELECT MC.\"MB_CODIGO\" FROM \"METODO_BRUTO\" MB, \"METODO_CARACTERISTICA\" MC WHERE (MB.\"MB_CODIGO\"=1) OR (MC.\"BG_CODIGO\"=1) OR (MB.\"MB_CODIGO\"=MC.\"MB_CODIGO\") OR (MB.\"MB_CODIGO\"<>0))";    
            Selecao = "SELECT CODIGO,IMPLEMENTACAO FROM METODO";
            //System.out.println(Selecao);
            Comando.executeQuery(Selecao);
            Resultado = Comando.getResultSet();
            //String diretorioArquivos="C:\\mallet\\bin\\code\\feature\\";
            String diretorioArquivos="C:\\temporario6\\jedit\\";            
                
            ExecutorService pool = Executors.newFixedThreadPool(4);
            List<Future<String>> threadResults = new ArrayList<Future<String>>();
            long id=1;
            while (Resultado.next())
            {
                String caminho = diretorioArquivos+"metodo-"+Resultado.getInt(1)+".txt";
                String implementacao = Resultado.getString(2);                


                Callable<String> callable = new Codigo(implementacao, caminho, id);
                Future<String> future = pool.submit(callable);
                threadResults.add(future);
                id++;
                try {
                    System.out.println(future.get());
                } catch (InterruptedException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (ExecutionException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }  
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
