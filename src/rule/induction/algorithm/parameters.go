package algorithm

import (
	"flag"
	"io/ioutil"
	"log"

	"gopkg.in/yaml.v2"
)

type parameters struct {
	Threshold   int    `yaml:"threshold"`
	StarSetSize int    `yaml:"starSetSize"`
	DatasetPath string `yaml:"datasetPath"`
}

// Parameters of the CN2 algorithm.
// The values are obtained from config-file or cli parameters
var Parameters parameters

func init() {
	// try to read config file. Nothing wrong appens if it does not exists
	if config, err := ioutil.ReadFile("cn2.config.yml"); err == nil {
		// try to unmarshal yaml file and exit if some error is found
		if err = yaml.Unmarshal(config, &Parameters); err != nil {
			log.Fatalf("error: %v", err)
		}
	} else {
		Parameters.Threshold = 90
		Parameters.StarSetSize = 15
		// TODO maybe default dataset?
	}

	// management of command line flags
	threshold := flag.Int("t", Parameters.Threshold, "TODO usage description")
	starSetSize := flag.Int("s", Parameters.StarSetSize, "TODO usage description")
	datasetPath := flag.String("d", Parameters.DatasetPath, "TODO usage description")

	// parse input arguments
	flag.Parse()

	// set parameters values
	Parameters.Threshold = *threshold
	Parameters.StarSetSize = *starSetSize
	Parameters.DatasetPath = *datasetPath
}
