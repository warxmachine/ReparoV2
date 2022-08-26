package in.cybersin.reparo.model;

public class Reqeust {
    String Type, Problem, Time;

    public Reqeust(String type, String problem, String time) {
        Type = type;
        Problem = problem;
        Time = time;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getProblem() {
        return Problem;
    }

    public void setProblem(String problem) {
        Problem = problem;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public Reqeust() {
    }


}
