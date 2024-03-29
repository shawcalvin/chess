package chess.ChessRuleBook;

import java.util.Collection;
import java.util.Objects;

import chess.*;

public abstract class MovementRule {
    protected ChessPiece.PieceType pieceType;

    abstract Collection<ChessMove> getValidMoves(ChessBoard board, ChessPosition position);

    public boolean positionIsOnBoard(ChessPosition position) {
        return position.getRow() > 0 && position.getRow() <=8 && position.getColumn() > 0 && position.getColumn() <= 8;
    }

    public boolean positionIsEmpty(ChessBoard board, ChessPosition position) {
        return positionIsOnBoard(position) && board.getPiece(position) == null;
    }

    public boolean positionIsCapturable(ChessBoard board, ChessPosition startPosition, ChessPosition endPosition) {
        return positionIsOnBoard(endPosition) && !positionIsEmpty(board, endPosition) && board.getPiece(startPosition).getTeamColor() != board.getPiece(endPosition).getTeamColor();
    }

    public void validateAndAddMove(ChessBoard board, ChessMove move, Collection<ChessMove> validMoves) {
        if (positionIsOnBoard(move.getEndPosition()) && (positionIsEmpty(board, move.getEndPosition()) || positionIsCapturable(board, move.getStartPosition(), move.getEndPosition()))) {
            validMoves.add(move);
        }
    }

    @Override
    public String toString() {
        return "MovementRule{" +
                "pieceType=" + pieceType +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovementRule that = (MovementRule) o;
        return pieceType == that.pieceType;
    }
    @Override
    public int hashCode() {
        return Objects.hash(pieceType);
    }
}
