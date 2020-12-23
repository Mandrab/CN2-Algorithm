package numerical

import "math"

// delta for float comparison
const delta float64 = 1e-5

// OperatorKey is the type of 'Operators' map key
type OperatorKey string

// Operators defines types and function for cathegorical attribute comparison
var Operators map[OperatorKey]func(v1, v2 float64) bool = map[OperatorKey]func(v1, v2 float64) bool{
	"Equal": func(v1, v2 float64) bool {
		return math.Abs(v1-v2) < delta
	},
	"Different": func(v1, v2 float64) bool {
		return math.Abs(v1-v2) > delta
	},
	"LesserEqual": func(v1, v2 float64) bool {
		return v1-v2 < delta
	},
	"Greater": func(v1, v2 float64) bool {
		return v1-v2 > delta
	},
}
