package main

import (
	"bufio"
	"fmt"
	"log"
	"os"

	"github.com/Mandrab/CN2-Algorithm/src/config/cn2"
	"github.com/Mandrab/CN2-Algorithm/src/rule/induction/algorithm"
	"github.com/tobgu/qframe"
)

func main() {

	// open file in CSV form and load it as dataset
	dsfile, err := os.Open(cn2.DatasetPath())
	if err != nil {
		log.Fatalf("error: %v", err)
	}
	ds := qframe.ReadCSV(bufio.NewReader(dsfile))

	// start CN2 algorithm with specified parameters and dataset
	rules := algorithm.CN2(cn2.Threshold(), cn2.StarSetSize(), ds)
	fmt.Println(rules)
}
