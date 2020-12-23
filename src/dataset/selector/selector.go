package selector

import (
	"github.com/Mandrab/CN2-Algorithm/src/dataset/selector/operator/cathegorical"
	"github.com/Mandrab/CN2-Algorithm/src/dataset/selector/operator/numerical"
)

type selector interface{}

// Cathegorical selector
type Cathegorical struct {
	Attribute string
	Cathegory string
	operator  cathegorical.OperatorKey
}

// Numerical selector
type Numerical struct {
	Attribute string
	Value     int
	Operator  numerical.OperatorKey
}

// Cathegoricals define possible selectors for the cathegory
func Cathegoricals(attribute, cathegory string) (selectors []Cathegorical) {
	for key := range cathegorical.Operators {
		selectors = append(selectors, Cathegorical{attribute, cathegory, key})
	}
	return selectors
}

// Numericals define possible selectors for the value
func Numericals(attribute string, value int) (selectors []Numerical) {
	for key := range numerical.Operators {
		selectors = append(selectors, Numerical{attribute, value, key})
	}
	return selectors
}
