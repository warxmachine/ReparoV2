package in.cybersin.reparo.model;

public class Request {
    String Type;
    String Problem;
    String Time;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Request(String id) {
        Id = id;
    }

    String Id;

    public Request(String type, String problem, String time) {
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

    public Request() {
    }


}
