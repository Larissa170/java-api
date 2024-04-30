package med.voll.api.domain.consulta.validacoes.agendamento;


import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorMedicoConsultaComMesmoHorario implements ValidadorAgendamentoDeConsulta{
    @Autowired
    private ConsultaRepository repository;
    public void validar (DadosAgendamentoConsulta dados){
        var medicoPossuiOutraConsulta = repository.existsByMedicoIdAndDataAndMotivoCancelamentoIsNull(dados.idMedico(),dados.data());
        if(medicoPossuiOutraConsulta){
            throw new ValidacaoException("Medico já possui outra consulta para este horário");
        }

    }
}
