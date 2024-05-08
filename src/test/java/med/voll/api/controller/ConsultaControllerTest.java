package med.voll.api.controller;

import med.voll.api.domain.consulta.AgendaDeConsultas;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.consulta.DadosDetalhamentoConsulta;
import med.voll.api.domain.medico.Especialidade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ConsultaControllerTest {
    @Autowired
    private MockMvc mvc; //simula requisicoes http
    @Autowired
    private JacksonTester<DadosAgendamentoConsulta> dadosAgendamentoConculstaJson; // objeto que simula um json

    @Autowired
    private JacksonTester<DadosDetalhamentoConsulta> dadosDetalhamentoConsultaJson;

    @MockBean
    private AgendaDeConsultas agendaDeConsultas;

    @Test
    @DisplayName("Deveria devolver 400 quando informações estao inválidas")
    @WithMockUser //mocka um usuario pois precisa de login
    void agendarCenario1() throws Exception {
       var response =  mvc.perform(post("/consultas"))
                .andReturn().getResponse(); //dispara uma requisicao e guarda o response

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value()); // compara o resultado com o status http 400
    }


    @Test
    @DisplayName("Deveria retonar 200 quando informações válidas")
    @WithMockUser
    void agendarCenario2() throws Exception{
        var data = LocalDateTime.now().plusHours(1);
        var especialidade = Especialidade.CARDIOLOGIA;
        var dadosDetalhamento =  new DadosDetalhamentoConsulta(null,1l,5l,data);

        when(agendaDeConsultas.agendar(any())).thenReturn(dadosDetalhamento); // quando criar a consulta usar esses dados mockados

        var response = mvc.perform(
                post("/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosAgendamentoConculstaJson.write(
                                new DadosAgendamentoConsulta(1l,5l,data,especialidade)
                        ).getJson()) // passa o json
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var jsonEsperado = dadosDetalhamentoConsultaJson.write(
               dadosDetalhamento
        ).getJson();
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
     }
}