/*

claim: {
    claim_number: 123,
    claim: "",
    referenced_claim_number: 123
}

**/

public class Claim {
    private int claimNumber;
    private String claim;
    private int referencedClaimNumber;

    public int getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(int claimNumber) {
        this.claimNumber = claimNumber;
    }

    public String getClaim() {
        return claim;
    }

    public void setClaim(String claim) {
        this.claim = claim;
    }

    public int getReferencedClaimNumber() {
        return referencedClaimNumber;
    }

    public void setReferencedClaimNumber(int referencedClaimNumber) {
        this.referencedClaimNumber = referencedClaimNumber;
    }
}