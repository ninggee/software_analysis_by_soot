package test;

import soot.*;
import soot.jimple.JimpleBody;
import soot.jimple.Stmt;
import soot.options.Options;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.util.Chain;

import java.util.Collections;
import java.util.Iterator;

public class Test {

    private static void init(String path) {
        soot.G.reset();
        Options options = Options.v();

        options.set_allow_phantom_refs(true); // allow soot class creation for missing classes
        options.set_prepend_classpath(true);
        options.set_validate(true);
        options.set_output_format(Options.output_format_jimple);
        options.set_src_prec(Options.src_prec_java); // only java are accepted for soot analysis
        options.set_keep_line_number(true);
        options.set_no_bodies_for_excluded(true);
        options.set_process_dir(Collections.singletonList(path));
        Scene.v().loadNecessaryClasses();
    }


    public static void main(String[] args) {
        String path = "src";
        init(path);

        SootClass appclass = Scene.v().loadClassAndSupport("examples.airline.Bug");

        SootMethod method = appclass.getMethodByName("run");
        printCallFromMethod(method);
    }

    public static void printCallFromMethod(SootMethod method) {
        JimpleBody jimpleBody = (JimpleBody)method.retrieveActiveBody();

        UnitGraph g = new BriefUnitGraph(jimpleBody);
        Chain<Local> locals = jimpleBody.getLocals();

        Local local = jimpleBody.getThisLocal();

        Iterator<Unit> it = g.iterator();

        while(it.hasNext()) {
            Stmt stmt = (Stmt)it.next();
            if(stmt.containsInvokeExpr()) {
                System.out.println("the invoke is : " + stmt.toString());
                System.out.println("the method is : " + stmt.getInvokeExpr().getMethod());
            }
        }
    }
}
