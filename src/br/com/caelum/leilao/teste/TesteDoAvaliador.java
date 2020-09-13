package br.com.caelum.leilao.teste;

import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;
import br.com.caelum.leilao.servico.Avaliador;
import br.com.caelum.leilao.servico.CriadorDeLeilao;

/* assertEquals(esperado, calculado) para podermos inverter assertEquals(calculado, esperado) utilizamo um framework chamado Hamcrest: assertThat
 * De: assertEquals(250.0, leiloeiro.getMenorLance(), 0.00001)
 * Para: assertThat(leiloeiro.getMenorLance(), equalTo(250.0));
 */

public class TesteDoAvaliador {
	
	private Avaliador leiloeiro;
	private Usuario joao;
	private Usuario jose;
	private Usuario maria;
	
	@Before // executa este método antes dos métodos de cada testes
	public void criaAvaliador() {
		this.leiloeiro = new Avaliador();
		this.joao = new Usuario("João");
		this.jose = new Usuario("José");
		this.maria = new Usuario("Maria");
	}

    @Test
    public void deveEntenderLancesEmOrdemCrescente() {

        Leilao leilao = new CriadorDeLeilao()
            .para("Playstation 3 Novo")
            .lance(joao, 250)
            .lance(jose, 300)
            .lance(maria, 400)
            .constroi();

        leiloeiro.avalia(leilao);

        assertThat(leiloeiro.getMaiorLance(), equalTo(400.0));
        assertThat(leiloeiro.getMenorLance(), equalTo(250.0));
    }

    @Test
    public void deveEntenderLeilaoComApenasUmLance() {
        Leilao leilao = new CriadorDeLeilao()
        .para("Playstation 3 Novo")
        .lance(joao, 1000)
        .constroi();

        leiloeiro.avalia(leilao);

        assertEquals(1000.0, leiloeiro.getMaiorLance(), 0.00001);
        assertEquals(1000.0, leiloeiro.getMenorLance(), 0.00001);
    }
	
	@Test
    public void deveEncontrarOsTresMaioresLances() {
        Leilao leilao = new CriadorDeLeilao()
            .para("Playstation 3 Novo")
            .lance(joao, 100)
            .lance(maria, 200)
            .lance(joao, 300)
            .lance(maria, 400)
            .constroi();

        leiloeiro.avalia(leilao);

        List<Lance> maiores = leiloeiro.getTresMaiores();
        assertEquals(3, maiores.size());
        
        assertThat(maiores, hasItems(
        		new Lance(maria, 400),
        		new Lance(joao, 300),
        		new Lance(maria, 200)
        	));
    }
	
	@Test(expected=RuntimeException.class) // Só ficará verde se a execção for lançada expected=RuntimeException.class subistitui o try cath
	public void naoDeveAvaliarLeiloesSemNenhumLanceDado() {
			Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo").constroi();
			
			leiloeiro.avalia(leilao);
	}
	
}
