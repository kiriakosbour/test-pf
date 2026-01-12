package gr.deddie.pfr.model;

/**
 * Created by M.Masikos on 21/4/2017.
 */
public class Question {
    private Long id;
    private Long question_id;
    private String text;
    private Long failure_id;
    private String answer;

    public Question() {
    }

    public Question(Long id, Long question_id, String text, Long failure_id, String answer) {
        this.id = id;
        this.question_id = question_id;
        this.text = text;
        this.failure_id = failure_id;
        this.answer = answer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(Long question_id) {
        this.question_id = question_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getFailure_id() {
        return failure_id;
    }

    public void setFailure_id(Long failure_id) {
        this.failure_id = failure_id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
