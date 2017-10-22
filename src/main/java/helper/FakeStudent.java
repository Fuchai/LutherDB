package helper;
import io.bloco.faker.Faker;
import lutherdb.Student;

/**
 * Created by JasonHu on 10/15/17.
 */
public class FakeStudent {


    Student record;
    Faker faker;


    public FakeStudent(){
        this.faker= new Faker();
    }

    public static void main(String[] args) {
        FakeStudent fs= new FakeStudent();
        String[] output = fs.getNames(100);
        for (String s: output){
            System.out.println(s);
        }
    }

    public String[] getNames(int howMany){
        String[] nameList=new String[howMany];

        for (int i = 0; i<howMany; i++){
            nameList[i]=this.faker.name.firstName()+" "+this.faker.name.lastName();
        }

        return nameList;
    }

}
