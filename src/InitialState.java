public class InitialState implements InputFileState {
    @Override
    public void extractIdentifier(InputFile inputFile) {
        String identifier = Utils.IdentifierExtractor.getIdentifier(inputFile.getSubmission());
        inputFile.setIdentifier(identifier);

        switch (identifier) {
            case "SET_ASIDE" -> inputFile.setState(new SetAsideState());
            case "DISCARDED" -> inputFile.setState(new DiscardedState());
            default -> inputFile.setState(new PrimedState());
        }
    }
}