package kr.makeappsgreat.onlinemall.user.member;

import kr.makeappsgreat.onlinemall.model.Address;
import org.modelmapper.ModelMapper;

class TestMember {

    private Member member;
    private String username;

    public TestMember(ModelMapper modelMapper) {
        new TestMember(modelMapper, "김가연", "simpleuser@email.com");
    }
    public TestMember(ModelMapper modelMapper, String name, String username) {
        AgreementRequest agreementRequest = new AgreementRequest();
        agreementRequest.setTerms1(true);
        agreementRequest.setTerms2(true);
        agreementRequest.setTerms3(true);
        agreementRequest.setMarketing(false);
        Agreement agreement = modelMapper.map(agreementRequest, Agreement.class);

        Address address = new Address();
        address.setZipcode("42700");
        address.setAddress("대구광역시 달서구");

        MemberRequest memberRequest = new MemberRequest();
        memberRequest.setName(name);
        memberRequest.setEmail(username);
        memberRequest.setPassword("simple");
        memberRequest.setAddress(address);
        memberRequest.setMobileNumber("010-1234-5678");

        this.member = modelMapper.map(memberRequest, Member.class);
        this.member.setAgreement(agreement);
    }

    public String getUsername() {
        return username;
    }

    public Member getMember() {
        return member;
    }
}