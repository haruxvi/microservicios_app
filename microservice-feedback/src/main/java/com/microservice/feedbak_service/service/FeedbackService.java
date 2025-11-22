
@Service
@RequiredArgsConstructor
@Transactional
public class FeedbackService {

    private final FeedbackRepository repository;

    public FeedbackResponse crear(FeedbackRequest req) {
        Feedback f = new Feedback();
        f.setUsuarioId(req.usuarioId());
        f.setMensaje(req.mensaje());
        f.setTipo(req.tipo().toUpperCase());
        f.setDestino(req.destino().toUpperCase());

        Feedback guardado = repository.save(f);
        return toResponse(guardado);
    }

    public List<FeedbackResponse> todos() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public List<FeedbackResponse> pendientes() {
        return repository.findByResueltoFalseOrderByFechaDesc()
                .stream().map(this::toResponse).toList();
    }

    public FeedbackResponse resolver(Long id) {
        Feedback f = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback no encontrado"));
        f.setResuelto(true);
        return toResponse(repository.save(f));
    }

    private FeedbackResponse toResponse(Feedback f) {
        return new FeedbackResponse(
            f.getIdFeedback(), f.getMensaje(), f.getTipo(),
            f.getDestino(), f.getFecha(), f.isResuelto(), f.getUsuarioId()
        );
    }
}