package med.voll.api.domain.paciente;

import med.voll.api.domain.endereco.Endereco;

public record DadosDetalhamentoPaciente(Long id,String nome, String telefone, String cpf, String email,  Endereco endereco) {
    public DadosDetalhamentoPaciente (Paciente paciente){
        this(paciente.getId(), paciente.getNome(), paciente.getTelefone(), paciente.getCpf(), paciente.getEmail(), paciente.getEndereco());
    }
}
