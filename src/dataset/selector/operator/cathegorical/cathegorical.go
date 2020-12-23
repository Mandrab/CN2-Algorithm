package cathegorical

// OperatorKey is the type of 'Operators' map key
type OperatorKey string

// Operators defines types and function for cathegorical attribute comparison
var Operators map[OperatorKey]func(v1, v2 string) bool = map[OperatorKey]func(v1, v2 string) bool{
	"Equal": func(v1, v2 string) bool {
		return v1 == v2
	},
	"Different": func(v1, v2 string) bool {
		return v1 != v2
	},
}
