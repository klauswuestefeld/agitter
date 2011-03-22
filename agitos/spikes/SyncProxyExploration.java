import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import guardachuva.agitos.shared.SessionToken;
import guardachuva.agitos.shared.UserDTO;
import guardachuva.agitos.shared.ValidationException;
import guardachuva.agitos.shared.rpc.RemoteApplication;

import org.junit.Ignore;
import org.junit.Test;

import com.gdevelop.gwt.syncrpc.SyncProxy;

public class SyncProxyExploration {
	
	private static final String AGITOSWEB_DEV = "http://127.0.0.1:8888/agitosweb/";
	private static final String AGITOSWEB_PROD = "http://www.vagaloom.com/agitosweb/";

	@Test
//	@Ignore
	public void problemWithMultipleContacts() throws ValidationException, Exception {
		RemoteApplication _application = (RemoteApplication) SyncProxy.newProxyInstance(
				RemoteApplication.class, AGITOSWEB_PROD, "rpc");
		SessionToken session = _application.authenticate("juan@email.com", "power3");
		_application.addContactsToMe(session, "juan.bernabo@igphone.com , joer00@gmail.com , patrick@gdp.com.br , andreb.picelli@terra.com.br ,", "");
//		_application.addContactsToMe(session, "juan.bernabo@igphone.com , joer00@gmail.com , patrick@gdp.com.br , andreb.picelli@terra.com.br , rh@confluence.com.br , eduardo@cqa.com.br , oportunidades@elumini.com.br , lwt@telestar.com.br , bcazarini@brq.com , jonas@holdenrh.com.br , rh@unione.com.br , sandra.nascimento@ppware.com.br , catia.higa@h2m.com.br , rh@k2consulting.com.br , julianavr@primeinformatica.com.br , bruno.stefani@ogeda.com.br , selecaoprofessor@gmail.com , lsanchez@gpnet.com.br , alvaro.caetano@metainf.com.br , vagas@cadmus.com.br , rhsp2@unisystem.com.br , selecao@plinformatica.com.br , neide@5a.com.br , brunno@confiantec.com.br , simone.silvestrini@accenture.com , rh@accurate.com.br , andrea.diniz@boldcron.com.br , viduedo@imago.com.br , rs04@sao.politec.com.br , naranayama@hotmail.com , rh@paggo.com.br , fbelem@dba.com.br , andrea.rh@dbktecnologia.com.br , debora.costa@projetomaior.com.br , rhjava@bankware.com.br , mauricio.gimenes@s2k.com.br , efs.edson@globo.com , rh@peopleconsulting.com.br , novostalentos@quality.com.br , rh@solvo.com.br , ivan@contmatic.com.br , smachado@grupofoco.com.br , rh@gwe.com.br , rh.sp@informaker.com.br , daniel.avizu@mro.com , preis@wa.com.br , paulabidio@brq.com , carmen.oliveira@procwork.com.br , RHRS@cwi.com.br , alecupolo@hotmail.com , consultorarh3@alfa-cs.com.br , vivian.redondo@drm.com.br , consultorrh1@alfa-cs.com.br , walkrh@gmail.com , daniel.marcos@triadsystems.com.br , elaine@globalti.com.br , milena.ribeiro@dtslatin.com , amanda.bosco@solvo.com.br , luciana.pita@trust.com.br , gustavo.abib@icatel.com.br , jobs-rio@sknt.com , java@uolinc.com , fmeirelles@stefanini.com.br , laura.gouveia@drm.com.br , ana.barros@atosorigin.com , carolina@cadmus.com.br , ednei.coutinho@resource.com.br , rh@triadsystems.com.br , rh@pro-cards.com.br , soraya@sbrconsulting.com.br , rubiene@nd10.com , caroline.vernizzi@ogeda.com.br , vgema@springwireless.net , auditeste@auditeste.com.br , andre.pirollo@cma.com.br , rh@deal.com.br , eneidalima@cvc.com.br , tecnologia@ymf.com.br , erico@weblinebrasil.com.br , rhtech@lantechinfo.com.br , apgoncalves@gpnet.com.br , elisangela.nunes@softtek.com , lucasl@mobilestreams.com , curriculum@cilix.com.br , recrutamento@zinia.com.br , c.oshiro@accenture.com , eric@auditeste.com.br , roberta.souza@h2m.com.br , marciarocha@ubsistemas.com.br , rh@ss7.com.br , rgobbo@ionsix.com.br , helen.costa@trust.com.br , gerber@intalio.com , hugo@biolinkbr.com , thais@tempoesolucoes.com.br , listac@yahoogrupos.com.br , marco@auditeste.com.br , angeli@contagemregresiva.com.br , angeli@contagemregresiva.com , ogosman@histaintl.com , fgarcia@uniban.br , cristiano@cepheus.com.br", "");
		UserDTO[] contacts = _application.getContactsForMe(session);
		assertThat(contacts.length, equalTo(105));
	}

}
