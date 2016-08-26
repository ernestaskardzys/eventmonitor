package info.ernestas.eventmonitor.model;

public class JsonResult<T> {

    private ResultStatus status;
    private T data;

    public JsonResult() {

    }

    public JsonResult(T data) {
        this(ResultStatus.SUCCESS, data);
    }

    public JsonResult(ResultStatus status, T data) {
        this.status = status;
        this.data = data;
    }

    public ResultStatus getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JsonResult<?> result = (JsonResult<?>) o;

        if (status != result.status) {
            return false;
        }
        return data.equals(result.data);

    }

    @Override
    public int hashCode() {
        int result = status.hashCode();
        result = 31 * result + data.hashCode();
        return result;
    }

}
