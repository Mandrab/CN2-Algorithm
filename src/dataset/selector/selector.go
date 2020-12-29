package selector

import (
	"github.com/Mandrab/CN2-Algorithm/src/dataset/selector/operator/cathegorical"
	"github.com/Mandrab/CN2-Algorithm/src/dataset/selector/operator/numerical"
)

// Selector represents a way to say if a data/attribute
// fulfill (or not) a condition.
type Selector interface {
	Attribute() string
	Fulfill(interface{}) (bool, error)
}

// Cathegoricals define possible selectors for the cathegory
func Cathegoricals(attribute, cathegory string) (selectors []Selector) {
	for key := range cathegorical.Operators {
		selectors = append(selectors, Cathegorical{attribute, cathegory, key})
	}
	return selectors
}

// Numericals define possible selectors for the value
func Numericals(attribute string, value float64) (selectors []Selector) {
	for key := range numerical.Operators {
		selectors = append(selectors, Numerical{attribute, value, key})
	}
	return selectors
}
