package main

import (
	"bufio"
	"fmt"
	"log"
	"os"

	"github.com/Mandrab/CN2-Algorithm/src/rule/induction/algorithm"
	"github.com/tobgu/qframe"
)

func main() {
	// get algorithm parameters
	parameters := algorithm.Parameters

	// load dataset in CSV form
	dsfile, err := os.Open(parameters.DatasetPath)
	if err != nil {
		log.Fatalf("error: %v", err)
	}
	ds := qframe.ReadCSV(bufio.NewReader(dsfile))

	// start CN2 algorithm with specified parameters and dataset
	rules := algorithm.CN2(parameters, ds)
	fmt.Println(rules)
}
