@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
@Tag(name = "Feedback", description = "Reportes y sugerencias de usuarios")
public class FeedbackController {

    private final FeedbackService service;

    @PostMapping
    public ResponseEntity<FeedbackResponse> crear(@Valid @RequestBody FeedbackRequest req) {
        return ResponseEntity.ok(service.crear(req));
    }

    @GetMapping
    public List<FeedbackResponse> todos() {
        return service.todos();
    }

    @GetMapping("/pendientes")
    public List<FeedbackResponse> pendientes() {
        return service.pendientes();
    }

    @PatchMapping("/{id}/resolver")
    public ResponseEntity<FeedbackResponse> resolver(@PathVariable Long id) {
        return ResponseEntity.ok(service.resolver(id));
    }
}