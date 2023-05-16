package org.example.helpers;

public record Vector2D(int x, int y) {

    public Vector2D add(Vector2D v){
        return new Vector2D(x + v.x(), y + v.y());
    }
    public Vector2D multiply(int n){
        return new Vector2D(x * n, y * n);
    }
}
