package set

type any interface{}

type complex []any

// Rule is used for classification
type Rule struct {
	complex complex
	class   any
}

// DecisionList is an ordered set of rules
type DecisionList []Rule
