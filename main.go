package main

import (
	"errors"
	"fmt"
	"math/rand"
)

func transpose(x [][]float32) [][]float32 {
	out := make([][]float32, len(x[0]))
	for i := 0; i < len(x); i += 1 {
		for j := 0; j < len(x[0]); j += 1 {
			out[j] = append(out[j], x[i][j])
		}
	}
	return out
}

func dot(x, y [][]float32) ([][]float32, error) {
	if len(x[0]) != len(y) {
		return nil, errors.New("Can't do matrix multiplication.")
	}

	out := make([][]float32, len(x))
	for i := 0; i < len(x); i += 1 {
		for j := 0; j < len(y); j += 1 {
			if len(out[i]) < 1 {
				out[i] = make([]float32, len(y))
			}
			out[i][j] += x[i][j] * y[j][i]
		}
	}
	return out, nil
}

func main() {
	const row int = 100
	const collumn int = 100

	var A [row][collumn]int

	for i := 0; i < row; i++ {
		for j := 0; j < collumn; j++ {
			A[i][j] = rand.Int()
		}
	}

	for i := 0; i < len(A); i++ {
		for j := 0; j < len(A[0]); j++ {
			fmt.Println(A[i][j])
		}
	}

}

