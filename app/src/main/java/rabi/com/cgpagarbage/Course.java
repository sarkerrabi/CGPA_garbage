package rabi.com.cgpagarbage;

/**
 * Created by RABI on 15-Mar-16.
 */
public class Course {

    private int _id;
    private String _courseName;
    private int _credit;
    private float _gpa;

    public Course( String _courseName, int _credit, float _gpa) {

        this._courseName = _courseName;
        this._credit = _credit;
        this._gpa = _gpa;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_courseName() {
        return _courseName;
    }

    public void set_courseName(String _courseName) {
        this._courseName = _courseName;
    }

    public int get_credit() {
        return _credit;
    }

    public void set_credit(int _credit) {
        this._credit = _credit;
    }

    public float get_gpa() {
        return _gpa;
    }

    public void set_gpa(float _gpa) {
        this._gpa = _gpa;
    }
}

