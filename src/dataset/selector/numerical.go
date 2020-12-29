package selector

import (
	"errors"

	"github.com/Mandrab/CN2-Algorithm/src/dataset/selector/operator/numerical"
)

// Numerical selector
type Numerical struct {
	attribute string
	value     float64
	operator  numerical.OperatorKey
}

// Attribute returns the name of attribute tested by the selector
func (selector Numerical) Attribute() string {
	return selector.attribute
}

// Fulfill checks if the given element fulfill the selector.
// It considers only the value: any other control on the attribute
// type should be made before calling this function.
// In case the 'value' type is different from 'float64' the
// function returns false and an error.
func (selector Numerical) Fulfill(value interface{}) (bool, error) {
	// type check on the passed value (must be a float)
	if floatValue, ok := value.(float64); ok {
		comparisonFunction := numerical.Operators[selector.operator]
		return comparisonFunction(floatValue, selector.value), nil
	}
	return false, errors.New("Tested value is not of type 'float64'")
}
