package com.vn.thu.configs;

import java.util.List;

import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;

/**
 * Mac dinh Spring for GraphQL che thong bao loi that (chi tra ve
 * "INTERNAL_ERROR" chung chung) de tranh lo thong tin nhay cam.
 * Vi day la loi nghiep vu (vd: "Category da ton tai", "Khong tim thay san pham")
 * nen can hien thi ro cho client thay - tuong tu ApiResponse ben REST.
 */
@Component
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {

	@Override
	protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
		return GraphqlErrorBuilder.newError(env)
				.message(ex.getMessage() != null ? ex.getMessage() : "Có lỗi xảy ra")
				.errorType(ErrorType.BAD_REQUEST)
				.build();
	}

	@Override
	protected List<GraphQLError> resolveToMultipleErrors(Throwable ex, DataFetchingEnvironment env) {
		return List.of(resolveToSingleError(ex, env));
	}
}
