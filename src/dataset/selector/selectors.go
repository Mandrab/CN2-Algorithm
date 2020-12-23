package selector

import (
	"log"

	"github.com/tobgu/qframe"
)

// Selectors generates all the possible selectors for a given dataframe
func Selectors(ds qframe.QFrame) (selectors []selector) {

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
				selectors = append(selectors, Numericals(columnName, values.ItemAt(valueIdx)))
			}
		} else if columnType == "string" {
			values, error := column.StringView(columnName)
			if error != nil {
				log.Fatalf("error: %v", error)
			}
			for valueIdx := 0; valueIdx < values.Len(); valueIdx++ {
				selectors = append(selectors, Cathegoricals(columnName, *values.ItemAt(valueIdx)))
			}
		}
		//fmt.Printf("class %v", columnValues.ColumnNames())
		//fmt.Println(columnValues.Len())
		//fmt.Println(selectors)
	}

	selectors = append(selectors, Numerical{"", 5, ""})
	return selectors
}
