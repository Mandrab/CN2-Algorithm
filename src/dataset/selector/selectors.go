package selector

import (
	"fmt"
	"log"

	"github.com/tobgu/qframe"
)

// Selectors generates all the possible selectors for a given dataframe
func Selectors(ds qframe.QFrame) (selectors []Selector) {

	attributes := ds.ColumnNames()[1:]
	for _, columnName := range attributes {
		column := ds.Select(columnName)
		columnType := column.ColumnTypes()[0]
		column = column.Distinct()

		if columnType == "int" {
			values, error := column.IntView(columnName)
			if error != nil {
				log.Fatalf("error: %v", error)
			}
			for valueIdx := 0; valueIdx < values.Len(); valueIdx++ {
				numericals := Numericals(columnName, float64(values.ItemAt(valueIdx)))
				selectors = append(selectors, numericals...)
			}
		} else if columnType == "string" {
			values, error := column.StringView(columnName)
			if error != nil {
				log.Fatalf("error: %v", error)
			}
			for valueIdx := 0; valueIdx < values.Len(); valueIdx++ {
				cathegoricals := Cathegoricals(columnName, *values.ItemAt(valueIdx))
				selectors = append(selectors, cathegoricals...)
			}
		}
	}

	for _, s := range selectors {
		fmt.Println(s)
	}

	return selectors
}
