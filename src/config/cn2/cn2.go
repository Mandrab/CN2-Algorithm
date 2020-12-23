package cn2

import (
	"flag"      // cli parameters
	"io/ioutil" // files management
	"log"       // logging

	"gopkg.in/yaml.v2" // yaml management
)

// CN2 algorithm's parameters
var parameters = struct {
	Threshold   int    `yaml:"threshold"`
	StarSetSize int    `yaml:"starSetSize"`
	DatasetPath string `yaml:"dataset"`
}{99, 15, ""}

// Threshold TODO
func Threshold() int {
	return parameters.Threshold
}

// StarSetSize TODO
func StarSetSize() int {
	return parameters.StarSetSize
}

// DatasetPath TODO
func DatasetPath() string {
	return parameters.DatasetPath
}

func init() {

	// try to read config file. Nothing wrong appens if it does not exists
	if config, err := ioutil.ReadFile("cn2.config.yml"); err == nil {
		// try to unmarshal yaml file and exit if some error is found
		if err = yaml.Unmarshal(config, &parameters); err != nil {
			log.Fatalf("error: %v", err)
		}
	}

	// management of command line flags
	flag.IntVar(&parameters.Threshold, "t", parameters.Threshold, "TODO usage description")
	flag.IntVar(&parameters.StarSetSize, "s", parameters.StarSetSize, "TODO usage description")
	flag.StringVar(&parameters.DatasetPath, "d", parameters.DatasetPath, "TODO usage description")

	// parse input arguments
	flag.Parse()
}
