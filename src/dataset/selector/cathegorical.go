package selector

import (
	"errors"

	"github.com/Mandrab/CN2-Algorithm/src/dataset/selector/operator/cathegorical"
)

// Cathegorical selector
type Cathegorical struct {
	attribute string
	value     string
	operator  cathegorical.OperatorKey
}

// Attribute returns the name of attribute tested by the selector
func (selector Cathegorical) Attribute() string {
	return selector.attribute
}

// Fulfill checks if the given element fulfill the selector.
// It considers only the value: any other control on the attribute
// type should be made before calling this function.
// In case the 'value' type is different from 'string' the function
// returns false and an error.
func (selector Cathegorical) Fulfill(value interface{}) (bool, error) {
	// type check on the passed value (must be a string)
	if stringValue, ok := value.(string); ok {
		comparisonFunction := cathegorical.Operators[selector.operator]
		return comparisonFunction(stringValue, selector.value), nil
	}
	return false, errors.New("Tested value is not of type 'string'")
}
