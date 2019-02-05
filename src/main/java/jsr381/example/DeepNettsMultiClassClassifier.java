package jsr381.example;

import deepnetts.data.DataSet;
import deepnetts.net.FeedForwardNetwork;
import deepnetts.net.layers.activation.ActivationType;
import deepnetts.net.loss.LossType;
import deepnetts.net.train.BackpropagationTrainer;
import deepnetts.util.Tensor;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DeepNettsMultiClassClassifier extends MultiClassClassifier <FeedForwardNetwork> {

    @Override
    public Map<String, Float> classify(float[] input) {
      FeedForwardNetwork model = getModel();
      model.setInput(Tensor.create(1, input.length, input)); //TODO: put array to input tensor placeholder
      float[] outputs = model.getOutput();
      String[] labels = model.getOutputLabels();
      Map<String, Float> result = new HashMap<>();
      for(int i=0; i<outputs.length; i++) {
          result.put(labels[i], outputs[i]);
      }
      return result;
    }

    public static Builder builder() {
        return new Builder();
    }

    // TODO: add static builder class and method

   public static class Builder implements javax.visrec.util.Builder<DeepNettsMultiClassClassifier> {
        private DeepNettsMultiClassClassifier product = new DeepNettsMultiClassClassifier();

        private float learningRate = 0.01f;
        private float maxError = 0.03f;
        private int maxEpochs = 1000;
        private int inputsNum;
        private int outputsNum;
        private int[] hiddenLayers;

        private DataSet<?> trainingSet;

        @Override
        public DeepNettsMultiClassClassifier build() {
            // Network architecture as Map/properties, json?
            FeedForwardNetwork model =  FeedForwardNetwork.builder()
                                          .addInputLayer(inputsNum)
                                          .addFullyConnectedLayer(3)
                                          .addFullyConnectedLayer(3)
                                          .addFullyConnectedLayer(3)
                                          .addOutputLayer(outputsNum, ActivationType.SOFTMAX)
                                          .lossFunction(LossType.CROSS_ENTROPY)
                                          .withActivationFunction(ActivationType.TANH)
                                          .build();

            BackpropagationTrainer trainer = new BackpropagationTrainer(); // model as param in constructor
            trainer.setLearningRate(learningRate)
                    .setMaxError(maxError);

            if (trainingSet!=null)
                trainer.train(model, trainingSet); // move model to constructor

            product.setModel(model);

            return product;
        }

        public Builder learningRate(float learningRate) {
            this.learningRate = learningRate;
            return this;
        }

        public Builder maxError(float maxError) {
            this.maxError = maxError;
            return this;
        }

        public Builder maxEpochs(int maxEpochs) {
            this.maxEpochs = maxEpochs;
            return this;
        }

        public Builder inputsNum(int inputsNum) {
            this.inputsNum = inputsNum;
            return this;
        }

        public Builder outputsNum(int outputsNum) {
            this.outputsNum = outputsNum;
            return this;
        }

        public Builder hiddenLayerSizes(int... hiddenLayers) {
            this.hiddenLayers = hiddenLayers;
            return this;
        }

        public Builder trainingSet(DataSet<?> trainingSet) {
            this.trainingSet = trainingSet;
            return this;
        }

        @Override
        public DeepNettsMultiClassClassifier build(Properties prop) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
   }
}