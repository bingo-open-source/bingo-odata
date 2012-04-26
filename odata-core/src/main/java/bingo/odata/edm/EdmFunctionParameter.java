package bingo.odata.edm;

import bingo.odata.zinternal.lang.ImmutableList;

/**
 * A CSDL Parameter element.
 *
 * <p>A Parameter element is used to define input and output parameters for function imports that are declared in CSDL.
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/ee473431.aspx">[msdn] Parameter Element (CSDL)</a>
 */
public class EdmFunctionParameter extends EdmItem {

    /** Parameter type: In, Out, or InOut */
    public enum Mode {
        In, Out, InOut;
    };

    private final String  name;
    private final EdmType type;
    private final Mode    mode;

    private EdmFunctionParameter(String name, EdmType type, Mode mode, EdmDocumentation doc, ImmutableList<EdmAnnotation<?>> annots) {
        super(doc, annots);
        this.name = name;
        this.type = type;
        this.mode = mode;
    }

    public String getName() {
        return name;
    }

    public EdmType getType() {
        return type;
    }

    public Mode getMode() {
        return mode;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(EdmFunctionParameter functionParameter, BuilderContext context) {
        return context.newBuilder(functionParameter, new Builder());
    }

    /** Mutable builder for {@link EdmFunctionParameter} objects. */
    public static class Builder extends EdmItem.Builder<EdmFunctionParameter, Builder> {

        private String                name;
        private EdmType               type;
        private EdmType.Builder<?, ?> typeBuilder;
        private Mode                  mode;

        @Override
        Builder newBuilder(EdmFunctionParameter functionParameter, BuilderContext context) {
            return new Builder().setName(functionParameter.name).setType(functionParameter.type).setMode(functionParameter.mode);
        }

        public EdmFunctionParameter build() {
            return new EdmFunctionParameter(name, typeBuilder != null ? typeBuilder.build() : type, mode, getDocumentation(), ImmutableList.copyOf(getAnnotations()));
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setType(EdmType type) {
            this.type = type;
            return this;
        }

        public Builder setType(EdmType.Builder<?, ?> typeBuilder) {
            this.typeBuilder = typeBuilder;
            return this;
        }

        public Builder setMode(Mode mode) {
            this.mode = mode;
            return this;
        }

        public Builder input(String name, EdmType type) {
            this.mode = Mode.In;
            this.name = name;
            this.type = type;
            return this;
        }

    }

}
