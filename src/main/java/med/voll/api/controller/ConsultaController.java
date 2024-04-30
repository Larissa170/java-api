package med.voll.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import med.voll.api.domain.consulta.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("consultas")
@SecurityRequirement(name="bearer-key")
public class ConsultaController {
    @Autowired
    private AgendaDeConsultas agenda;

    @Autowired
    private ConsultaRepository consultaRepository;

    @PostMapping
    @Transactional
    public ResponseEntity agendar(@RequestBody @Valid DadosAgendamentoConsulta dados) {
        var dto = agenda.agendar(dados);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity cancelar(@RequestBody @Valid DadosCancelamentoConsulta dados){
        agenda.cancelar(dados);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoConsulta>> listar(@PageableDefault(size = 10) Pageable paginacao){
        var page = consultaRepository.findAllByMotivoCancelamentoIsNull(paginacao).map(DadosDetalhamentoConsulta::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id) {
        var consulta = consultaRepository.getReferenceById(id);
        return ResponseEntity.ok( new DadosDetalhamentoConsulta(consulta));
    }

}
