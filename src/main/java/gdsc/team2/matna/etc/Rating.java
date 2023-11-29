package gdsc.team2.matna.etc;




public enum Rating {
    PERFECT(5),
    GOOD(4),
    SOSO(3),
    NOTGOOD(2),
    BAD(1);

    private final Integer value;

    Rating(Integer value) {
        this.value = value;
    }
    public Integer getPoint() {
        return value;
    }
}
