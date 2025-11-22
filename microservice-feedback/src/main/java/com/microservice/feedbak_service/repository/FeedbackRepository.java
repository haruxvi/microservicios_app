


public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByResueltoFalseOrderByFechaDesc();
    List<Feedback> findByUsuarioIdOrderByFechaDesc(Long usuarioId);
}