package com.almasb.patterns.visitor;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class App extends Application {

    private Parent createContent() {
        Pane root = new Pane();

        root.setPrefSize(800, 600);

        List<Node> nodes = Arrays.asList(
                new Circle(50, 50, 50),
                new Rectangle(40, 50),
                new Line(20, 20, 33, 44)
        );

        nodes.forEach(n -> {
            n.setTranslateX(Math.random() * 300);
            n.setTranslateY(Math.random() * 400);
        });

        NodeVisitor visitor = new ColorChangingVisitor();

        nodes.stream().map(VisitableNode::new).forEach(n -> {
            n.visit(visitor);
        });

        root.getChildren().addAll(nodes);

        return root;
    }

    static class VisitableNode<T extends Node> {
        private T node;

        public VisitableNode(T node) {
            this.node = node;
        }

        public void visit(NodeVisitor visitor) {

            try {
                Method method = visitor.getClass().getMethod("visit", node.getClass());
                method.invoke(visitor, node);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    interface NodeVisitor {
        void visit(Circle circle);
        void visit(Rectangle rectangle);
        void visit(Line line);
    }

    static class ColorChangingVisitor implements NodeVisitor {

        @Override
        public void visit(Circle circle) {
            circle.setFill(Color.RED);
        }

        @Override
        public void visit(Rectangle rectangle) {
            rectangle.setFill(Color.BLUE);
        }

        @Override
        public void visit(Line line) {
            line.setStrokeWidth(15);
            line.setStroke(Color.BLUEVIOLET);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static class Launcher {
        public static void main(String[] args) {
            App.main(args);
        }
    }
}
