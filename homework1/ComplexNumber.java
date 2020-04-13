public class ComplexNumber {

    private double re, im;

    public ComplexNumber() {
        this(0, 0);
    }

    public ComplexNumber(double re, double im){
        this.re = re;
        this.im = im;
    }

    public void setRe(double re) {
        this.re = re;
    }

    public void setIm(double im) {
        this.im = im;
    }

    public double getRe() {
        return re;
    }

    public double getIm() {
        return im;
    }

    @Override
    public String toString() {
        return "(" + re + ", " + im + ")";
    }

    public ComplexNumber sum(ComplexNumber number){
        ComplexNumber result = new ComplexNumber();
        result.re = this.getRe() + number.getRe();
        result.im = this.getIm() + number.getIm();
        return result;
    }

    public ComplexNumber sub(ComplexNumber number){
        ComplexNumber result = new ComplexNumber();
        result.re = this.getRe() - number.getRe();
        result.im = this.getIm() - number.getIm();
        return result;
    }

    public ComplexNumber mul(ComplexNumber number){
        ComplexNumber result = new ComplexNumber();
        result.re = this.getRe()*number.getRe() - this.getIm()*number.getIm();
        result.im = this.getRe()*number.getIm() + this.getIm()*number.getRe();
        return result;
    }

    public ComplexNumber div(ComplexNumber number){
        ComplexNumber result = new ComplexNumber();
        double divider = number.getRe()*number.getRe() + number.getIm()*number.getIm();
        result.re = this.getRe()*number.getRe() - this.getIm()*(-1)*number.getIm();
        result.im = this.getRe()*(-1)*number.getIm() + this.getIm()*number.getRe();
        result.re /= divider;
        result.im /= divider;
        return result;
    }

}
