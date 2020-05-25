import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import org.apache.commons.lang3.StringUtils;
import org.openide.util.Exceptions;

public class Codigo implements Callable<String> {

    private String implementacao;
    private String caminho;
    private long id;

    
    
    public Codigo(String implementacao, String caminho, long id)
    {
        this.implementacao = implementacao;
        this.caminho = caminho;
        this.id = id;

    }

    @Override
    public String call()
    {

        String conteudoSeparado[] = this.implementacao.split(" ");
        
        for (int i=0;i<=(conteudoSeparado.length-1);i++)
        {
            conteudoSeparado[i] = conteudoSeparado[i].trim(); 
            conteudoSeparado[i] = StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(conteudoSeparado[i]), ' ');
        }
                                       
       
        StringBuilder tmp = new StringBuilder();   
        for (int i=0;i<=(conteudoSeparado.length-1);i++)
        {
             tmp.append(" " + conteudoSeparado[i].trim());
        }
                        

        this.implementacao=tmp.toString();
        
        FileWriter fw = null;
        try
        {
            fw = new FileWriter(this.caminho, true);            
            fw.write(this.implementacao);
            fw.close();            
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return this.caminho;
    }
}